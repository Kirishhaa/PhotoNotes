package com.kirishhaa.photonotes.presentation.auth.localusersscreen

data class LocalUsersScreenStateUI(
    val users: List<LocalUserUI>? = null,
    val loading: Boolean = true
)