package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

data class MarkerUI(
    val id: Int,
    val userId: Int,
    val folderId: Int?,
    val folderName: String?,
    val name: String,
    val filePath: String,
    val location: LocationUI,
    val tags: List<MarkerTagUI>,
    val description: String?
)

data class MarkerTagUI(
    val name: String,
    val id: Int
)