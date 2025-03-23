package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

import com.kirishhaa.photonotes.domain.Language

data class LanguageState(
    val loading: Boolean = true,
    val language: LanguageUI? = null
) {

    fun requireLanguage() = requireNotNull(language)

}