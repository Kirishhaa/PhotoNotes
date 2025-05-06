package com.kirishhaa.photonotes.data.repos.markers

import com.kirishhaa.photonotes.data.db.entity.LocationEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerTagEntity
import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.Marker

class MarkerEntityMapper {

    fun map(
        markerEntity: MarkerEntity,
        locationEntity: LocationEntity?,
        tagsEntity: List<MarkerTagEntity>,
        folderName: String?,
    ): Marker {
        return Marker(
            id = markerEntity.id,
            userId = markerEntity.userId,
            folderId = markerEntity.folderId,
            folderName = folderName,
            name = markerEntity.name,
            filePath = markerEntity.filePath,
            location = locationEntity?.toDomainLocation() ?: DomainLocation.NONE,
            tags = tagsEntity.map { it.toMarkerTag() },
            description = markerEntity.description
        )
    }

    fun map(marker: Marker): MarkerEntity = MarkerEntity(
        id = marker.id,
        userId = marker.userId,
        folderId = marker.folderId,
        name = marker.name,
        filePath = marker.filePath,
        description = marker.description
    )

}