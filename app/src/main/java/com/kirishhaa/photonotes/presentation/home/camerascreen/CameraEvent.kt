package com.kirishhaa.photonotes.presentation.home.camerascreen

sealed interface CameraEvent {

    class OnNewImageCaptured(val markerId: Int): CameraEvent

}