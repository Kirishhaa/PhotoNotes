package com.kirishhaa.photonotes.presentation.profile.changepasswordscreen

data class ChangePasswordState(
    val loading: Boolean = true,
    val currentPasswordError: Boolean = false,
    val passwordsNotSameError: Boolean = false
)