package com.kirishhaa.photonotes.domain.location

import com.kirishhaa.photonotes.domain.DomainLocation

class GetLocationUseCase(
    private val locationRepository: LocationRepository
) {


    suspend fun execute(): DomainLocation? {
        return locationRepository.getCurrentLocation()
    }

}