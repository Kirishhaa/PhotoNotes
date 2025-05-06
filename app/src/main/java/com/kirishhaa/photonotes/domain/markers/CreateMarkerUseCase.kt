package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.exceptions.UserNotFoundException
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import kotlinx.coroutines.flow.first

class CreateMarkerUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val markersRepository: MarkersRepository
) {

    //returns marker id
    suspend fun execute(filePath: String): Int {
        val userId =
            localUsersRepository.getEnteredUser().first()?.id ?: throw UserNotFoundException()
        markersRepository.createMarker(userId, filePath)
        return markersRepository.getMarkerIdByFilePath(userId, filePath)
    }

}