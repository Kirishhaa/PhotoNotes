package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import kotlinx.coroutines.flow.first

class ImageCapturedUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val markersRepository: MarkersRepository
) {

    //returns marker id
    suspend fun execute(filePath: String, location: DomainLocation?): Int {
        val id = localUsersRepository.getEnteredUser().first()?.id ?: 0
        return markersRepository.onImageCaptured(id, filePath, location)
    }

}