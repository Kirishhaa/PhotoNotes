package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Marker
import kotlinx.coroutines.flow.Flow

class GetMarkerByIdUseCase(
    private val markersRepository: MarkersRepository
) {

    fun execute(userId: Int, markerId: Int): Flow<Marker?> {
        return markersRepository.getMarkerById(userId, markerId)
    }

}