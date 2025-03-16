package com.kirishhaa.photonotes.presentation.navigation

sealed class MarkerDetailScreen(val route: String) {

    data object MarkerDetail: MarkerDetailScreen("MARKERDETAIL/{markerId}")

}