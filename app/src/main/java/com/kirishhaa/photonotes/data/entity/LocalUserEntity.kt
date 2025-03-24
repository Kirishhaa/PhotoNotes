package com.kirishhaa.photonotes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kirishhaa.photonotes.domain.Language

@Entity(
    tableName = "local_users"
)
class LocalUserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("image_path") val imagePath: String,
    @ColumnInfo("entered") val entered: Boolean,
    @ColumnInfo("remember") val remember: Boolean,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("login") val login: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("language")val language: Language
)