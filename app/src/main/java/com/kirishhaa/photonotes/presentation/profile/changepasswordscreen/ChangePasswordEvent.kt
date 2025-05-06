package com.kirishhaa.photonotes.presentation.profile.changepasswordscreen

sealed interface ChangePasswordEvent {

    class SendMessage(val message: String) : ChangePasswordEvent

}