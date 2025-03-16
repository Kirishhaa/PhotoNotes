package com.kirishhaa.photonotes.presentation.home.foldersscreen

import com.kirishhaa.photonotes.domain.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FolderMapper {

    suspend fun map(folder: Folder): FolderUI = withContext(Dispatchers.Default) {
        return@withContext FolderUI(folder.name)
    }

}