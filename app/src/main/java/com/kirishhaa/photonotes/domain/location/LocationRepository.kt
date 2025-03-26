package com.kirishhaa.photonotes.domain.location

import com.kirishhaa.photonotes.domain.DomainLocation

interface LocationRepository {

    suspend fun getCurrentLocation(): DomainLocation?

}