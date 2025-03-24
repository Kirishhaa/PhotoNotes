package com.kirishhaa.photonotes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "marker_tags",
    primaryKeys = ["marker_id","name"],
    foreignKeys = [
        ForeignKey(
            entity = MarkerEntity::class,
            parentColumns = ["id"],
            childColumns = ["marker_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class TagEntity(
    @ColumnInfo("marker_id") val markerId: Int,
    @ColumnInfo("name") val name: String
)