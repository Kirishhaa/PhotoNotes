package com.kirishhaa.photonotes.presentation.navigation

sealed class MarkerDetailScr(val route: String) {

    data object MarkerDetail : MarkerDetailScr("MARKERDETAIL/{markerId}")

}