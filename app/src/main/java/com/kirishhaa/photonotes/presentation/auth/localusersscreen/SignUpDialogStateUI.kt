package com.kirishhaa.photonotes.presentation.auth.localusersscreen

data class SignUpDialogStateUI(
    val loading: Boolean,
    val errorLogin: Boolean,
    val errorPassword: Boolean,
    val errorUsername: Boolean
)