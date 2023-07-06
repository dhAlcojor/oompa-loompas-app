package com.dhalcojor.oompaloompas.ui.oompaloompadetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dhalcojor.oompaloompas.R
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaDetails
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaFavorite
import com.dhalcojor.oompaloompas.data.oompaLoompaDetails

@Composable
fun OompaLoompaDetailScreen(
    navController: NavHostController,
    viewModel: OompaLoompaDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isLoading && uiState.oompaLoompa == null) {
        viewModel.fetchOompaLoompaDetails(navController.currentBackStackEntry?.arguments?.getInt("oompaLoompaId"))
    }

    Scaffold(
        topBar = {
            OompaLoompaDetailTopBar(
                uiState,
                goBack = { navController.popBackStack() }
            )
        },
    ) { paddingValues ->
        OompaLoompaDetailContent(
            uiState,
            paddingValues
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OompaLoompaDetailTopBar(uiState: OompaLoompaDetailUiState, goBack: () -> Unit) {
    val oompaLoompaName =
        uiState.oompaLoompa?.let { "${it.firstName} ${it.lastName}" } ?: stringResource(
            id = R.string.loading
        )
    TopAppBar(
        title = { Text(oompaLoompaName) },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back_alt)
                )
            }
        }
    )
}

@Composable
private fun OompaLoompaDetailContent(
    uiState: OompaLoompaDetailUiState,
    padding: PaddingValues
) {
    if (uiState.oompaLoompa == null) {
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (uiState.isLoading) stringResource(R.string.loading)
                else stringResource(R.string.generic_error_message),
            )
        }
    } else {
        val selectedTabIndex = remember { mutableStateOf(0) }
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTabIndex.value
            ) {
                Tab(
                    text = { Text(stringResource(R.string.basic_tab)) },
                    selected = selectedTabIndex.value == 0,
                    onClick = { selectedTabIndex.value = 0 }
                )
                Tab(
                    text = { Text(stringResource(R.string.quota_tab)) },
                    selected = selectedTabIndex.value == 1,
                    onClick = { selectedTabIndex.value = 1 }
                )
                Tab(
                    text = { Text(stringResource(R.string.favorite_tab)) },
                    selected = selectedTabIndex.value == 2,
                    onClick = { selectedTabIndex.value = 2 }
                )
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                val oompaLoompa = uiState.oompaLoompa
                when (selectedTabIndex.value) {
                    0 -> BasicTabContent(oompaLoompa)
                    1 -> QuotaTabContent(oompaLoompa.quota)
                    2 -> FavoriteTabContent(oompaLoompa.favorite)
                }
            }
        }
    }
}

@Composable
private fun BasicTabContent(oompaLoompa: OompaLoompaDetails) {
    val fullName = "${oompaLoompa.firstName} ${oompaLoompa.lastName}"
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = oompaLoompa.image,
            placeholder = ColorPainter(MaterialTheme.colorScheme.primary),
            contentDescription = "$fullName image",
            modifier = Modifier.fillMaxWidth()
        )
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                fullName,
                style = MaterialTheme.typography.titleLarge
            )
            Text(oompaLoompa.email, style = MaterialTheme.typography.bodySmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(Modifier.padding(vertical = 8.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(oompaLoompa.profession, style = MaterialTheme.typography.bodyLarge)
                Text(oompaLoompa.country, style = MaterialTheme.typography.bodyLarge)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(id = R.string.age_label, oompaLoompa.age),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(id = R.string.gender_label, oompaLoompa.gender),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(R.string.height_label, oompaLoompa.height),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                oompaLoompa.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun QuotaTabContent(quota: String) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Text(
            quota,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun FavoriteTabContent(favorite: OompaLoompaFavorite) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.color_label, favorite.color),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                stringResource(R.string.food_label, favorite.food),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            stringResource(R.string.random_title),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        Text(
            favorite.randomString,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            stringResource(R.string.song_title),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Divider(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            favorite.song,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

private val previewOompaLoompa = oompaLoompaDetails

@Preview(showBackground = true)
@Composable
private fun OompaLoompaDetailsScreen() {
    val previewUiState = OompaLoompaDetailUiState(
        false,
        emptyList(),
        previewOompaLoompa,
    )
    Scaffold(
        topBar = {
            OompaLoompaDetailTopBar(
                previewUiState,
                goBack = { }
            )
        },
    ) { paddingValues ->
        OompaLoompaDetailContent(
            previewUiState,
            paddingValues
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OompaLoompaTabRow() {
    OompaLoompaDetailContent(
        OompaLoompaDetailUiState(
            false,
            emptyList(),
            previewOompaLoompa,
        ),
        PaddingValues(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun OompaLoompaFavorite() {
    FavoriteTabContent(favorite = previewOompaLoompa.favorite)
}