package com.kirishhaa.photonotes.domain

import com.kirishhaa.photonotes.domain.exceptions.EnteredUserNotExistException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class ImageCapturedUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val imagesRepository: ImagesRepository
) {

    //returns marker id
    suspend fun execute(filePath: String, location: DomainLocation?): Int {
        val id = localUsersRepository.getEnteredUser().first()?.id ?: 0
        return imagesRepository.onImageCaptured(id, filePath, location)
    }

}