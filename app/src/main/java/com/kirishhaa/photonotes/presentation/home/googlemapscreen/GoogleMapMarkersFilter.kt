package com.kirishhaa.photonotes.presentation.home.googlemapscreen

import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.Marker

class GoogleMapMarkersFilter {

    fun filter(markers: List<Marker>): List<Marker> {
        return markers.filter { it.location != DomainLocation.NONE }
    }

}