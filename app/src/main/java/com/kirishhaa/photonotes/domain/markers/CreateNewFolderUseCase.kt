package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Folder

class CreateNewFolderUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(folder: Folder) = markersRepository.onCreateNewFolder(folder)

}