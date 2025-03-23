package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Marker

class UpdateMarkerUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(marker: Marker) {
        markersRepository.updateMarker(marker)
    }

}