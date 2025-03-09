package com.kirishhaa.photonotes.presentation.localusersscreen

data class LocalUsersStateUI(
    val screenState: LocalUsersScreenStateUI = LocalUsersScreenStateUI(),
    val signInDialogState: SignInDialogStateUI? = null,
    val signUpDialogState: SignUpDialogStateUI? = null,
)