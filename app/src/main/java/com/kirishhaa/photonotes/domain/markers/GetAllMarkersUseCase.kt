package com.kirishhaa.photonotes.domain.markers

class GetAllMarkersUseCase(
    private val markersRepository: MarkersRepository
) {

    fun execute(userId: Int) = markersRepository.getAllMarkersByUserId(userId)

}