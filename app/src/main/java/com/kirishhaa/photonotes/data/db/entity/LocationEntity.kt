package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kirishhaa.photonotes.domain.DomainLocation

@Entity(
    tableName = "location",
    foreignKeys = [
        ForeignKey(
            entity = MarkerEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("latitude") val latitude: Double,
    @ColumnInfo("longitude") val longitude: Double,
    @ColumnInfo("country") val country: String,
    @ColumnInfo("town") val town: String
) {

    fun toDomainLocation(): DomainLocation = DomainLocation(
        latitude = latitude,
        longitude = longitude,
        country = country,
        town = town
    )

}