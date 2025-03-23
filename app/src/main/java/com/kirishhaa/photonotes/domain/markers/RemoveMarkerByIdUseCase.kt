package com.kirishhaa.photonotes.domain.markers

class RemoveMarkerByIdUseCase(
    private val markersRepository: MarkersRepository
) {

    suspend fun execute(markerId: Int, userId: Int) {
        markersRepository.removeMarkerById(markerId, userId)
    }

}