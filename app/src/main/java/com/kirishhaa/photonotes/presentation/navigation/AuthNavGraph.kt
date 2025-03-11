package com.kirishhaa.photonotes.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kirishhaa.photonotes.presentation.auth.localusersscreen.LocalUsersScreen

fun NavGraphBuilder.AuthNavGraph(navController: NavController) {
    navigation(
        route = GraphRoute.AUTH,
        startDestination = AuthScreen.LoginScreen.route
    ) {
        composable(route = AuthScreen.LoginScreen.route) {
            LocalUsersScreen(
                toHomeScreen = { navController.navigate(GraphRoute.HOME) {
                    popUpTo(GraphRoute.AUTH) { inclusive = true }
                    launchSingleTop = true
                } }
            )
        }
    }
}