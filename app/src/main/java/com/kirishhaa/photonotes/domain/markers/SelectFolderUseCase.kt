package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Folder

class SelectFolderUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(markerId: Int, userId: Int, folderName: String?) {
        markersRepository.selectFolder(markerId, userId, folderName)
    }

}