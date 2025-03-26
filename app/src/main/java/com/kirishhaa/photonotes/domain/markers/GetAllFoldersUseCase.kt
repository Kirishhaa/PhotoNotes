package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Folder
import kotlinx.coroutines.flow.Flow

class GetAllFoldersUseCase(
    private val markersRepository: MarkersRepository
) {

    fun execute(userId: Int): Flow<List<Folder>> {
        return markersRepository.getAllFoldersByUserId(userId)
    }

}