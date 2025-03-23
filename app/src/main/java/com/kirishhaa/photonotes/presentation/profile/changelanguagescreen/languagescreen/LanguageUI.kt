package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

import com.kirishhaa.photonotes.domain.Language

class LanguageUI(
    private val language: Language
) {

    fun getAsStringResource(): String = language.name

}