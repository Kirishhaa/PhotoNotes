package com.kirishhaa.photonotes.presentation.profile.changeusernamescreen

sealed interface ChangeUsernameEvent {

    data object UsernameWasChanged : ChangeUsernameEvent

}