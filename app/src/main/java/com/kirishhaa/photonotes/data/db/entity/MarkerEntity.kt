package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "photo",
    foreignKeys = [
        ForeignKey(
            entity = LocalUserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folder_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("user_id") val userId: Int,
    @ColumnInfo("folder_id") val folderId: Int?,
    @ColumnInfo("title") val name: String,
    @ColumnInfo("photo_path") val filePath: String,
    @ColumnInfo("description") val description: String?
)