package com.kirishhaa.photonotes.domain.markers

class SelectFolderUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(markerId: Int, userId: Int, folderId: Int?) {
        markersRepository.selectFolder(markerId, userId, folderId)
    }

}