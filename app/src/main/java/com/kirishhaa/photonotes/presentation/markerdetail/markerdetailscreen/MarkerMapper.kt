package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.Marker
import com.kirishhaa.photonotes.domain.MarkerTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarkerMapper {

    suspend fun map(marker: Marker): MarkerUI = withContext(Dispatchers.Default) {
        MarkerUI(
            id = marker.id,
            userId = marker.userId,
            folderId = marker.folderId,
            folderName = marker.folderName,
            name = marker.name,
            filePath = marker.filePath,
            location = marker.location.toLocationUI(),
            tags = marker.tags.map { MarkerTagUI(it.name, it.id) },
            description = marker.description
        )
    }

    suspend fun map(markerUI: MarkerUI): Marker = withContext(Dispatchers.Default) {
        Marker(
            id = markerUI.id,
            userId = markerUI.userId,
            folderId = markerUI.folderId,
            folderName = markerUI.folderName,
            name = markerUI.name,
            filePath = markerUI.filePath,
            location = markerUI.location.toLocation(),
            tags = markerUI.tags.map { tag -> MarkerTag(tag.name, tag.id) },
            description = markerUI.description
        )
    }

    private fun LocationUI.toLocation(): DomainLocation {
        return DomainLocation(latitude, longitude, country, town)
    }

    private fun DomainLocation?.toLocationUI(): LocationUI {
        return LocationUI(
            latitude = this?.latitude ?: 0.0,
            longitude = this?.longitude ?: 0.0,
            country = this?.country ?: DomainLocation.UNKNOWN,
            town = this?.town ?: DomainLocation.UNKNOWN
        )
    }

}