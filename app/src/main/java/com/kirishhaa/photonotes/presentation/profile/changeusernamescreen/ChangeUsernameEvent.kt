package com.kirishhaa.photonotes.presentation.profile.changeusernamescreen

sealed interface ChangeUsernameEvent {

    data object UsernameWasChanged : ChangeUsernameEvent

    data object UserNotFound: ChangeUsernameEvent

    data object UserAlreadyExist: ChangeUsernameEvent

    data object ReadWrite: ChangeUsernameEvent

    data object WrongUsername: ChangeUsernameEvent

}