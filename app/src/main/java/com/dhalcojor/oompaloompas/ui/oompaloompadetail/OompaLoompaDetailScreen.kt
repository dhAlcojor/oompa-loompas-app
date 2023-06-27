package com.dhalcojor.oompaloompas.ui.oompaloompadetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun OompaLoompaDetailScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            OompaLoompaDetailTopBar(
                "Oompa Loompa ${
                    navController.currentBackStackEntry?.arguments?.getInt(
                        "oompaLoompaId"
                    )
                }",
                goBack = { navController.popBackStack() }
            )
        },
    ) { paddingValues ->
        OompaLoompaDetailContent(modifier = Modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OompaLoompaDetailTopBar(oompaLoompaName: String, goBack: () -> Unit) {
    TopAppBar(title = { Text(oompaLoompaName) }, navigationIcon = {
        IconButton(onClick = goBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
        }
    })
}

@Composable
private fun OompaLoompaDetailContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Oompa Loompa Detail")
    }
}