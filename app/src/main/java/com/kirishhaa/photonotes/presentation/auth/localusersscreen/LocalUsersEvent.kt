package com.kirishhaa.photonotes.presentation.auth.localusersscreen

sealed interface LocalUsersEvent {

    data object EnteredUserExist : LocalUsersEvent

    data object WrongPasswordException : LocalUsersEvent

    data object WrongUsernameException : LocalUsersEvent

    data object WrongLoginException : LocalUsersEvent

    data object CloseApp : LocalUsersEvent

}