package com.kirishhaa.photonotes.presentation.profile.changeemailscreen

sealed interface ChangeEmailEvents {

    data object UserNotFound: ChangeEmailEvents

    data object WrongEmail: ChangeEmailEvents

    data object ReadWrite: ChangeEmailEvents

    data object EmailChanged: ChangeEmailEvents

}