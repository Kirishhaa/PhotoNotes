package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import com.kirishhaa.photonotes.domain.Marker

sealed interface MarkerDetailEvent {

    class ShowMessage(val message: String): MarkerDetailEvent

    data object GoBack: MarkerDetailEvent

}