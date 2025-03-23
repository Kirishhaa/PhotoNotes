package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

data class MarkerUI(
    val id: Int,
    val userId: Int,
    val folderName: String?,
    val name: String,
    val filePath: String,
    val location: LocationUI,
    val tags: List<String>,
    val saved: Boolean,
    val description: String?
)