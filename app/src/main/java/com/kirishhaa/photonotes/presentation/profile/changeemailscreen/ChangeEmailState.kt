package com.kirishhaa.photonotes.presentation.profile.changeemailscreen

data class ChangeEmailState(
    val loading: Boolean = true,
    val currentEmail: String = "",
    val currentEmailError: Boolean = false,
    val emailsNotSameError: Boolean = false
)