package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "markers",
    foreignKeys = [
        ForeignKey(
            entity = LocalUserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = FolderEntity::class,
        parentColumns = ["name"],
        childColumns = ["folder_name"],
        onDelete = ForeignKey.CASCADE,
    )
    ]
)
data class MarkerEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("user_id") val userId: Int,
    @ColumnInfo("folder_name") val folderName: String?,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("file_path") val filePath: String,
    @ColumnInfo("saved") val saved: Boolean,
    @ColumnInfo("description") val description: String?
)