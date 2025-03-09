package com.kirishhaa.photonotes.presentation.languagescreen

import com.kirishhaa.photonotes.domain.Language

class LanguageUI(
    private val language: Language
) {

    fun getAsStringResource(): String = language.name

    companion object {
        fun fromLanguage(language: Language): LanguageUI = LanguageUI(language)
    }

}