package com.kirishhaa.photonotes.presentation.home.camerascreen

data class CameraState(
    val loadingState: Boolean = true,
    val creatingNewMarker: Boolean = false,
    val requestCameraPermissionCount: Int = 0,
    val requestLocationPermissionCount: Int = 0
)