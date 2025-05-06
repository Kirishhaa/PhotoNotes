package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

sealed interface MarkerDetailEvent {

    class ShowMessage(val message: String) : MarkerDetailEvent

    data object GoBack : MarkerDetailEvent

}