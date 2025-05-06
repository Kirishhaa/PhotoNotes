package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kirishhaa.photonotes.domain.MarkerTag

@Entity(
    tableName = "phototag",
    foreignKeys = [
        ForeignKey(
            entity = MarkerEntity::class,
            parentColumns = ["id"],
            childColumns = ["photo_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
class MarkerTagEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("photo_id") val markerId: Int,
    @ColumnInfo("name") val name: String,
) {

    fun toMarkerTag(): MarkerTag = MarkerTag(name = name, id = id)

}