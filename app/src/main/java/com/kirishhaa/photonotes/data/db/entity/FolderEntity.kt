package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "folder",
    foreignKeys = [
        ForeignKey(
            entity = LocalUserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("user_id") val userId: Int,
    @ColumnInfo("title") val title: String
)