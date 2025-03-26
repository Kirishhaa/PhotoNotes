package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.kirishhaa.photonotes.domain.MarkerTag

@Entity(
    tableName = "marker_tags",
    primaryKeys = ["marker_id","name"],
    foreignKeys = [
        ForeignKey(
            entity = MarkerEntity::class,
            parentColumns = ["id"],
            childColumns = ["marker_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = LocalUserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )
    ]
)
class MarkerTagEntity(
    @ColumnInfo("marker_id") val markerId: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("user_id") val userId: Int
) {

    fun toMarkerTag(): MarkerTag = MarkerTag(name)

}