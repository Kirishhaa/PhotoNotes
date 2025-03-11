package com.kirishhaa.photonotes.presentation.auth.localusersscreen

data class LocalUsersStateUI(
    val screenState: LocalUsersScreenStateUI = LocalUsersScreenStateUI(),
    val signInDialogState: SignInDialogStateUI? = null,
    val signUpDialogState: SignUpDialogStateUI? = null,
)