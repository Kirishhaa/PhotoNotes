package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

import com.kirishhaa.photonotes.domain.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LanguageMapper {

    suspend fun map(language: Language): LanguageUI = withContext(Dispatchers.Default) {
        return@withContext LanguageUI(language = language)
    }

}