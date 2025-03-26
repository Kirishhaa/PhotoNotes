package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.domain.LocalUser

@Entity(
    tableName = "local_users",
    foreignKeys = [
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["name"],
            childColumns = ["language_name"]
        )
    ]
)
data class LocalUserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("image_path") val imagePath: String,
    @ColumnInfo("entered") val entered: Boolean,
    @ColumnInfo("remember") val remember: Boolean,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("login") val login: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("language_name") val languageName: String
)