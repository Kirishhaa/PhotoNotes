package com.kirishhaa.photonotes.presentation.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kirishhaa.photonotes.presentation.home.camerascreen.CameraScreen
import com.kirishhaa.photonotes.presentation.home.feedbackscreen.FeedbackScreen
import com.kirishhaa.photonotes.presentation.home.foldersscreen.FoldersScreen
import com.kirishhaa.photonotes.presentation.home.googlemapscreen.GoogleMapScreen


fun NavGraphBuilder.HomeNavGraph() {
    composable(GraphRoute.HOME) {
        val navController = rememberNavController()
        HomeScaffold(navController = navController) { paddingValues ->
            HomeNavGraph(navController, paddingValues)
        }
    }
}

@Composable
private fun HomeNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        route = GraphRoute.HOME,
        startDestination = BottomNavigationScreen.Folders.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(route = BottomNavigationScreen.Folders.route) {
            val activity = LocalActivity.current
            FoldersScreen(
                onCloseApp = { activity?.finish() },
                toMarkerDetails = { markerId ->
                    navController.navigate(MarkerDetailScr.MarkerDetail.route.replace("{markerId}", markerId.toString()))
                }
            )
        }
        composable(route = BottomNavigationScreen.Camera.route) {
            CameraScreen(
                onNewImageCaptured = { markerId ->
                    navController.navigate(MarkerDetailScr.MarkerDetail.route.replace("{markerId}", markerId.toString()))
                }
            )
        }
        composable(route = BottomNavigationScreen.GoogleMap.route) {
            GoogleMapScreen(
                onMarkerClicked = { markerId ->
                    navController.navigate(MarkerDetailScr.MarkerDetail.route.replace("{markerId}", markerId.toString()))
                }
            )
        }
        composable(route = BottomNavigationScreen.Feedback.route) {
            FeedbackScreen()
        }
        MarkerDetailNavGraph(navController = navController)
        ProfileNavGraph(navController = navController)
    }
}