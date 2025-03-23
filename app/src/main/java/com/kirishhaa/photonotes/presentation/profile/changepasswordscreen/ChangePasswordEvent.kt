package com.kirishhaa.photonotes.presentation.profile.changepasswordscreen

sealed interface ChangePasswordEvent {

    data object PasswordWasChanged: ChangePasswordEvent

}