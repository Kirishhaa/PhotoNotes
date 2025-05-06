package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Folder

class RemoveFolderUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(id: Int, userId: Int, folderName: String) {
        val folder = Folder(id, folderName, userId)
        markersRepository.removeFolder(folder)
    }

}