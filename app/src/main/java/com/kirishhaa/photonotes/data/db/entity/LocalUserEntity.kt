package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    foreignKeys = [
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["id"],
            childColumns = ["lang_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalUserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("lang_id") val langId: Int,
    @ColumnInfo("photo_path") val imagePath: String?,
    @ColumnInfo("entered") val entered: Boolean,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("login") val login: String,
    @ColumnInfo("username") val name: String,
)