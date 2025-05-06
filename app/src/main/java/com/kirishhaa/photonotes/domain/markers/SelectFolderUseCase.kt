package com.kirishhaa.photonotes.domain.markers

class SelectFolderUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(markerId: Int, userId: Int, folderName: String?) {
        markersRepository.selectFolder(markerId, userId, folderName)
    }

}