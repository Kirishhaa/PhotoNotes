package com.kirishhaa.photonotes.presentation.profile.profilescreen

import com.kirishhaa.photonotes.domain.LocalUser

data class ProfileState(
    val loading: Boolean = true,
    val user: LocalUser? = null
)