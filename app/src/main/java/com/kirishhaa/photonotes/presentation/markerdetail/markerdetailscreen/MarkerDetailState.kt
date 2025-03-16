package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import com.kirishhaa.photonotes.domain.Marker

class MarkerDetailState(
    val preloading: Boolean = true,
    val marker: Marker? = null
) {

    fun requireMarker() = requireNotNull(marker)


}