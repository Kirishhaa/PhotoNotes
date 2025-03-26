package com.kirishhaa.photonotes.presentation.home.foldersscreen

import com.kirishhaa.photonotes.domain.Folder
import com.kirishhaa.photonotes.domain.Marker

data class FoldersState(
    val loadingState: Boolean = true,
    val showAddFolderDialog: Boolean = false,
    val showEditButton: Boolean = true,
    val inFolder: Boolean = false,
    val folders: List<FolderUI> = emptyList(),
    val markers: List<MarkerUI> = emptyList(),
)