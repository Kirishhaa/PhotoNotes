package com.kirishhaa.photonotes.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen.MarkerDetailScreen


fun NavGraphBuilder.MarkerDetailNavGraph(navController: NavHostController) {
    composable(route = MarkerDetailScr.MarkerDetail.route, arguments = listOf(navArgument("markerId") { type = NavType.IntType })) {
        val markerId = it.arguments?.getInt("markerId") ?: -1
        MarkerDetailScreen(markerId = markerId, goBack = { navController.popBackStack() })
    }
}