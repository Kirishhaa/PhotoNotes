package com.kirishhaa.photonotes.presentation.navigation

sealed class ProfileScreen(val route: String) {

    data object Profile : ProfileScreen("PROFILE_SCREEN")

    data object ChangeEmail : ProfileScreen("CHANGE_EMAIL_SCREEN")

    data object ChangePassword : ProfileScreen("CHANGE_PASSWORD_SCREEN")

    data object ChangeUsername : ProfileScreen("CHANGE_USERNAME_SCREEN")

    data object ChangeLanguage : ProfileScreen("CHANGE_LANGUAGE")

}