package com.kirishhaa.photonotes.presentation.home.foldersscreen

sealed interface FolderEvent {

    data object CloseApp: FolderEvent

}