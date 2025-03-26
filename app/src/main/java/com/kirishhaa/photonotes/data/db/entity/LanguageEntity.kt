package com.kirishhaa.photonotes.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kirishhaa.photonotes.domain.Language

@Entity(
    tableName = "languages"
)
class LanguageEntity(
    @ColumnInfo("name")
    @PrimaryKey val name: String
) {

    fun toLanguage(): Language = Language(name)

}