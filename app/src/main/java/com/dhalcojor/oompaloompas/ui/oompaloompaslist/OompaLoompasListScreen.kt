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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dhalcojor.oompaloompas.R
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.ui.theme.MyApplicationTheme

const val TAG = "OompaLoompasListScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OompaLoompasListScreen(
    navController: NavController,
    viewModel: OompaLoompasListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.oompaLoompasList.isEmpty() && !uiState.isLoading) {
        viewModel.fetchOompaLoompas()
    }

    Log.d(TAG, "uiState: $uiState")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oompa Loompas") },
                actions = {
                    IconButton(onClick = { viewModel.fetchOompaLoompas(uiState.currentPage - 1) }) {
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
                handlePrevious = { viewModel.fetchOompaLoompas(uiState.currentPage - 1) },
                handleNext = { viewModel.fetchOompaLoompas(uiState.currentPage + 1) }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),

                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 8.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.oompaLoompasList.size) { index ->
                    OompaLoompasListItem(
                        uiState.oompaLoompasList[index],
                    ) { navController.navigate("detail/${uiState.oompaLoompasList[index].id}") }
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

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
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
            GlideImage(
                model = item.image,
                contentDescription = "Oompa Loompa image",
                modifier = Modifier
                    .size(56.dp)
            ) {
                it.circleCrop()
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${item.firstName} ${item.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = item.profession, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Age: ${item.age}", style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    Text("Gender: ${item.gender}", style = MaterialTheme.typography.bodyMedium)
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
                        text = "${item.firstName} ${item.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = item.profession, style = MaterialTheme.typography.bodyMedium)
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
        //OompaLoompasApp(OompaLoompasListUiState(isLoading = true))
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        //OompaLoompasApp(OompaLoompasListUiState(oompaLoompasList = previewItems))
    }
}
