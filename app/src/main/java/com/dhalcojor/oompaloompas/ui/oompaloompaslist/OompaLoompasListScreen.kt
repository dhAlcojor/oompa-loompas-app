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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dhalcojor.oompaloompas.R
import com.dhalcojor.oompaloompas.ui.theme.MyApplicationTheme

@Composable
fun OompaLoompasListScreen(
    viewModel: OompaLoompasListViewModel = hiltViewModel()
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    when (items) {
        is OompaLoompasListUiState.Loading -> {
            Text("Loading...")
        }

        is OompaLoompasListUiState.Success -> {
            OompaLoompasListScreen(
                items = (items as OompaLoompasListUiState.Success).data,
            )
        }

        else -> {
            Text("Error")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OompaLoompasListScreen(
    items: List<OompaLoompaListItemState>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oompa Loompas") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_list_24),
                            contentDescription = "Filter",
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Previous page",
                    )
                }
                Text(text = "1")
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Next page",
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 8.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(modifier = Modifier.padding(8.dp), text = "${it.firstName} ${it.lastName}")
                }
            }
        }
    }
}

// Previews
val previewItems = listOf(
    OompaLoompaListItemState("Marcy", "Karadzas"),
    OompaLoompaListItemState("Kotlin", "Android"),
)

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        OompaLoompasListScreen(previewItems)
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        OompaLoompasListScreen(previewItems)
    }
}
