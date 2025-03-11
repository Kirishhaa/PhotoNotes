package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun HomeScaffold(navController: NavHostController, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = { HomeTopBar("Username", "userImagePath", navController = navController) },
        bottomBar = { HomeBottomBar(navController = navController) },
        containerColor = Color.LightGray
    ) { paddingValues ->
        content(paddingValues)
    }
}