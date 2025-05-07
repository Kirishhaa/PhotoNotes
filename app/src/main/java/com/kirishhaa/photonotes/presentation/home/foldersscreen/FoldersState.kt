package com.kirishhaa.photonotes.presentation.home.foldersscreen

import com.kirishhaa.photonotes.domain.Folder

data class FoldersState(
    val loadingState: Boolean = true,
    val showAddFolderDialog: Boolean = false,
    val showEditButton: Boolean = true,
    val selectedFolder: FolderUI? = null,
    val folders: List<FolderUI> = emptyList(),
    val markers: List<MarkerUI> = emptyList(),
)