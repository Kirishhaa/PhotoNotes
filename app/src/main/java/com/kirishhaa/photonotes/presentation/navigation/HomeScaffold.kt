package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun HomeScaffold(navController: NavHostController, content: @Composable (PaddingValues) -> Unit) {
    val viewmodel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val data by viewmodel.data.collectAsState()
    Scaffold(
        topBar = { HomeTopBar(data?.username ?: "", data?.photoPath, navController = navController) },
        bottomBar = { HomeBottomBar(navController = navController) },
        containerColor = Color.LightGray
    ) { paddingValues ->
        content(paddingValues)
    }
}