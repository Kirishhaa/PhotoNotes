package com.kirishhaa.photonotes.domain

class GetLocationUseCase(
    private val locationRepository: LocationRepository
) {


    suspend fun execute(): DomainLocation? {
        return locationRepository.getCurrentLocation()
    }

}