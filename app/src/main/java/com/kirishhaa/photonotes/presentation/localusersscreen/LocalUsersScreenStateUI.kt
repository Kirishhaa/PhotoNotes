package com.kirishhaa.photonotes.presentation.localusersscreen

data class LocalUsersScreenStateUI(
    val users: List<LocalUserUI>? = null,
    val loading: Boolean = true
)