package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Marker

class GetMarkerByIdUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(markerId: Int): Marker {
        return markersRepository.getMarkerByIdUseCase(markerId)
    }

}