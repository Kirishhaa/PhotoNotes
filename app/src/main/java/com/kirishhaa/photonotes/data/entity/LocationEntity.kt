package com.kirishhaa.photonotes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "locations",
    primaryKeys = ["marker_id","latitude","longitude"],
    foreignKeys = [
        ForeignKey(
            entity = MarkerEntity::class,
            parentColumns = ["id"],
            childColumns = ["marker_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class LocationEntity(
    @ColumnInfo("marker_id") val markerId: Int,
    @ColumnInfo("latitude") val latitude: Double,
    @ColumnInfo("longitude") val longitude: Double,
    @ColumnInfo("country") val country: String,
    @ColumnInfo("town") val town: String
)