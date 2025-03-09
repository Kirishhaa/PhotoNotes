package com.kirishhaa.photonotes.presentation.localusersscreen

data class SignUpDialogStateUI(
    val loading: Boolean,
    val errorLogin: Boolean,
    val errorPassword: Boolean,
    val errorUsername: Boolean
)