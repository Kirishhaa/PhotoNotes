package com.kirishhaa.photonotes.data.db

import androidx.room.TypeConverter
import com.kirishhaa.photonotes.domain.Language

class RoomTypeConverters {

    @TypeConverter
    fun convertLanguageToString(language: Language): String = language.name

    @TypeConverter
    fun convertStringToLanguage(value: String): Language = Language(value)

}