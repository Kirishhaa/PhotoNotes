package com.kirishhaa.photonotes.presentation.navigation

sealed class AuthScreen(
    val route: String
) {

    data object LoginScreen: AuthScreen(route = "LOGIN_SCREEN")

    data object ForgotPasswordScreen: AuthScreen(route = "FORGOT_PASSWORD_SCREEN")

}