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
        tagsEntity: List<MarkerTagEntity>
    ): Marker {
        return Marker(
            id = markerEntity.id,
            userId = markerEntity.userId,
            folderName = markerEntity.folderName,
            name = markerEntity.name,
            filePath = markerEntity.filePath,
            location = locationEntity?.toDomainLocation() ?: DomainLocation.NONE,
            saved = markerEntity.saved,
            tags = tagsEntity.map { it.toMarkerTag() },
            description = markerEntity.description
        )
    }

    fun map(marker: Marker): MarkerEntity = MarkerEntity(
        id = marker.id,
        userId = marker.userId,
        folderName = marker.folderName,
        name = marker.name,
        filePath = marker.filePath,
        saved = marker.saved,
        description = marker.description
    )

}