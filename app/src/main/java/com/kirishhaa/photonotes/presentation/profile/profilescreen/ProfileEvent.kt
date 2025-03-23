package com.kirishhaa.photonotes.presentation.profile.profilescreen

sealed interface ProfileEvent {

    data object ToAuth: ProfileEvent

}