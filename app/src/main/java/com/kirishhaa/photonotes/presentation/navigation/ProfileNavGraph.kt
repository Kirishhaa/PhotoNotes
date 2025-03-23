package com.kirishhaa.photonotes.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kirishhaa.photonotes.presentation.profile.changeemailscreen.ChangeEmailScreen
import com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.ChangeLanguageScreen
import com.kirishhaa.photonotes.presentation.profile.changepasswordscreen.ChangePasswordScreen
import com.kirishhaa.photonotes.presentation.profile.changeusernamescreen.ChangeUsernameScreen
import com.kirishhaa.photonotes.presentation.profile.profilescreen.ProfileScreen

fun NavGraphBuilder.ProfileNavGraph(navController: NavHostController) {
    navigation(
        route = GraphRoute.PROFILE,
        startDestination = ProfileScreen.Profile.route
    ) {
        composable(ProfileScreen.Profile.route) {
            ProfileScreen(
                onChangeLanguage = {
                    navController.navigate(ProfileScreen.ChangeLanguage.route)
                },
                onChangeName = {
                    navController.navigate(ProfileScreen.ChangeUsername.route)
                },
                onChangeEmail = {
                    navController.navigate(ProfileScreen.ChangeEmail.route)
                },
                onChangePas = {
                    navController.navigate(ProfileScreen.ChangePassword.route)
                },
                toAuth = {
                    navController.navigate(GraphRoute.AUTH) {
                        popUpTo(GraphRoute.ROOT) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(ProfileScreen.ChangeEmail.route) {
            ChangeEmailScreen()
        }
        composable(ProfileScreen.ChangePassword.route) {
            ChangePasswordScreen()
        }
        composable(ProfileScreen.ChangeUsername.route) {
            ChangeUsernameScreen()
        }
        composable(ProfileScreen.ChangeLanguage.route) {
            ChangeLanguageScreen()
        }
        AuthNavGraph(navController)
    }
}