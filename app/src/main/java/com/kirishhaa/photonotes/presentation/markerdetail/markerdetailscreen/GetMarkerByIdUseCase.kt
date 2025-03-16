package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import com.kirishhaa.photonotes.domain.ImagesRepository
import com.kirishhaa.photonotes.domain.Marker

class GetMarkerByIdUseCase(
    private val imagesRepository: ImagesRepository
) {

    suspend fun execute(markerId: Int): Marker {
        return imagesRepository.getMarkerByIdUseCase(markerId)
    }

}