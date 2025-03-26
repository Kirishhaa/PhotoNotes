package com.kirishhaa.photonotes.domain

class Marker(
    val id: Int,
    val userId: Int,
    val folderName: String?,
    val name: String,
    val filePath: String,
    val location: DomainLocation,
    val saved: Boolean,
    val tags: List<MarkerTag>,
    val description: String?
)