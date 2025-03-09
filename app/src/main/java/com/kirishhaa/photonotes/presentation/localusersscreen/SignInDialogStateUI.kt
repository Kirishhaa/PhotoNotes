package com.kirishhaa.photonotes.presentation.localusersscreen

data class SignInDialogStateUI(
    val chosenUser: LocalUserUI,
    val loading: Boolean,
    val errorLogin: Boolean,
    val errorPassword: Boolean
)