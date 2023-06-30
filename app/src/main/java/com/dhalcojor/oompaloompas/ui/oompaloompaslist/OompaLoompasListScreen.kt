/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhalcojor.oompaloompas.ui.oompaloompaslist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dhalcojor.oompaloompas.R
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.ui.theme.MyApplicationTheme

const val TAG = "OompaLoompasListScreen"

private fun filterBy(oompaLoompa: OompaLoompa, filter: String): Boolean {
    val res = if (filter.isBlank()) true
    else oompaLoompa[filter] == filter
    Log.d(TAG, "filterBy: $oompaLoompa -> $filter = $res")
    return res
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OompaLoompasListScreen(
    navController: NavController,
    viewModel: OompaLoompasListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val genders = remember { mutableListOf<String>() }
    val professions = remember { mutableListOf<String>() }
    var genderFilter by remember { mutableStateOf("") }
    var professionFilter by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (uiState.oompaLoompasList.isEmpty() && !uiState.isLoading) {
        viewModel.fetchOompaLoompas()
    }

    if (uiState.isLoading) {
        genderFilter = ""
        professionFilter = ""
    }

    if (uiState.oompaLoompasList.isNotEmpty()) {
        genders.clear()
        genders.add("")
        genders.addAll(uiState.oompaLoompasList.map { it.gender }.distinct().toMutableList())

        professions.clear()
        professions.add("")
        professions.addAll(
            uiState.oompaLoompasList.map { it.profession }.distinct().toMutableList()
        )
    }

    val conditions = listOf<((oompaLoompa: OompaLoompa) -> Boolean)>(
        { oompaLoompa -> filterBy(oompaLoompa, genderFilter) },
        { oompaLoompa -> filterBy(oompaLoompa, professionFilter) }
    )
    val filteredOompaLoompas = uiState.oompaLoompasList.filter { oompaLoompa ->
        conditions.all { it(oompaLoompa) }
    }.toMutableList()

    if (showDialog) {
        OompaLoompasFilterDialog(
            dismissDialog = { showDialog = false },
            genderFilter = genderFilter,
            setGenderFilter = { gender -> genderFilter = gender },
            genders = genders,
            professionFilter = professionFilter,
            setProfessionFilter = { profession -> professionFilter = profession },
            professions = professions,
        )
    }

    val onPrevPage = { viewModel.fetchOompaLoompas(uiState.currentPage - 1) }
    val onNextPage = { viewModel.fetchOompaLoompas(uiState.currentPage + 1) }
    val onGoToDetail: (id: Int) -> Unit = { id -> navController.navigate("detail/${id}") }

    OompaLoompasListLayout(
        uiState = uiState,
        filteredOompaLoompas = filteredOompaLoompas,
        onPrevPage = onPrevPage,
        onNextPage = onNextPage,
        onGoToDetail = onGoToDetail,
        toggleShowDialog = { showDialog = !showDialog },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OompaLoompasFilterDialog(
    dismissDialog: () -> Unit,
    genderFilter: String,
    setGenderFilter: (String) -> Unit,
    genders: List<String>,
    professionFilter: String,
    setProfessionFilter: (String) -> Unit,
    professions: List<String>,
) {
    AlertDialog(
        onDismissRequest = dismissDialog,
    ) {
        var genderDropdownExpanded by remember { mutableStateOf(false) }
        var professionDropdownExpanded by remember { mutableStateOf(false) }
        Card {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Filter your Oompa Loompas",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Button(onClick = { genderDropdownExpanded = true }) {
                        Text(genderFilter.ifBlank { "Filter by Gender" })
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Gender Filter")
                    }
                    DropdownMenu(
                        expanded = genderDropdownExpanded,
                        onDismissRequest = { genderDropdownExpanded = false }
                    ) {
                        genders.forEach { gender ->
                            DropdownMenuItem(
                                text = { Text(gender.ifBlank { "-" }) },
                                onClick = {
                                    setGenderFilter(gender)
                                    genderDropdownExpanded = false
                                })
                        }
                    }
                    Button(onClick = { professionDropdownExpanded = true }) {
                        Text(professionFilter.ifBlank { "Filter by Profession" })
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Profession Filter"
                        )
                    }
                    DropdownMenu(expanded = professionDropdownExpanded, onDismissRequest = {
                        professionDropdownExpanded = false
                    }) {
                        professions.forEach { profession ->
                            DropdownMenuItem(
                                text = { Text(profession.ifBlank { "-" }) },
                                onClick = {
                                    setProfessionFilter(profession)
                                    professionDropdownExpanded = false
                                })
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    Arrangement.End
                ) {
                    TextButton(onClick = dismissDialog) {
                        Text("Close".uppercase())
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OompaLoompasListLayout(
    uiState: OompaLoompasListUiState,
    filteredOompaLoompas: List<OompaLoompa>,
    onPrevPage: () -> Unit,
    onNextPage: () -> Unit,
    onGoToDetail: (id: Int) -> Unit,
    toggleShowDialog: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oompa Loompas") },
                actions = {
                    IconButton(onClick = toggleShowDialog) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_list_24),
                            contentDescription = "Filter",
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentPage = uiState.currentPage,
                totalPages = uiState.totalPages,
                handlePrevious = onPrevPage,
                handleNext = onNextPage,
            )
        }
    ) { padding ->
        if (uiState.isLoading || uiState.userMessages.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (uiState.isLoading) "Loading..."
                    else "Oops! Something went wrong.\nPlease, try again later."
                )
            }
        } else {
            val oompaLoompas = filteredOompaLoompas.ifEmpty { uiState.oompaLoompasList }
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 8.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(oompaLoompas.size) { index ->
                    OompaLoompasListItem(
                        oompaLoompas[index],
                    ) { onGoToDetail(oompaLoompas[index].id) }
                }
            }
        }
    }

}

@Composable
private fun BottomBar(
    currentPage: Int,
    totalPages: Int,
    handlePrevious: () -> Unit,
    handleNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        IconButton(onClick = { handlePrevious() }, enabled = currentPage > 1) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Previous page",
            )
        }
        Text(
            textAlign = TextAlign.Center,
            text = "$currentPage",
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = { handleNext() }, enabled = currentPage < totalPages) {
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "Next page",
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OompaLoompasListItem(item: OompaLoompa, handleOnClick: () -> Unit) {
    Card(
        onClick = { handleOnClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = "Oompa Loompa image",
                placeholder = ColorPainter(MaterialTheme.colorScheme.primary),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "${item.firstName} ${item.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = item.profession, style = MaterialTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gender: ${item.gender}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Age: ${item.age}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

// Previews
val previewItems = listOf(
    OompaLoompa(
        1,
        "Marcy",
        "Karadzas",
        "https://placehold.co/200",
        "Developer",
        24,
        "F"
    ),
    OompaLoompa(
        2,
        "Kotlin",
        "Android",
        "https://placehold.co/200",
        "Minion",
        141,
        "M"
    ),
)

@Preview(showBackground = true)
@Composable
private fun OompaLoompasBottomBar() {
    BottomBar(currentPage = 1, totalPages = 2, {}, {})
}

@Preview(showBackground = true)
@Composable
private fun ListItemPreview() {
    val item = previewItems[0]
    MyApplicationTheme {
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Oompa Loompa image",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "${item.firstName} ${item.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = item.profession, style = MaterialTheme.typography.bodyMedium)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gender: ${item.gender}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Age: ${item.age}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    MyApplicationTheme {
        OompaLoompasListLayout(
            OompaLoompasListUiState(isLoading = true),
            emptyList(),
            {},
            {},
            {},
            {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
    MyApplicationTheme {
        OompaLoompasListLayout(
            OompaLoompasListUiState(userMessages = listOf("Error")),
            emptyList(),
            {},
            {},
            {},
            {},
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        OompaLoompasListLayout(
            OompaLoompasListUiState(oompaLoompasList = previewItems),
            emptyList(),
            {},
            {},
            {},
            {},
        )
    }
}
