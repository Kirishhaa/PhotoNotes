package com.kirishhaa.photonotes.presentation.profile.changeemailscreen

sealed interface ChangeEmailEvents {

    data object DataWasChanged: ChangeEmailEvents

}