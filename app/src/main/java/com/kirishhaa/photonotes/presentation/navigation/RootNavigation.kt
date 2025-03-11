package com.kirishhaa.photonotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        route = GraphRoute.ROOT,
        startDestination = GraphRoute.AUTH
    ) {
        AuthNavGraph(navController = navController)
        HomeNavGraph()
    }
}

object GraphRoute {
    const val ROOT = "ROOT"
    const val AUTH = "AUTH"
    const val HOME = "HOME"
    const val PROFILE = "PROFILE"
}