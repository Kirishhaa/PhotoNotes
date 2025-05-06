package com.kirishhaa.photonotes.presentation.profile.changeemailscreen

sealed interface ChangeEmailEvents {

    class SendMessage(val message: String): ChangeEmailEvents

}