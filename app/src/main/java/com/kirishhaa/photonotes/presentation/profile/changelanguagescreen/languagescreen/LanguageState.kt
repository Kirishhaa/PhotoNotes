package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

data class LanguageState(
    val loading: Boolean = true,
    val language: LanguageUI? = null
) {

    fun requireLanguage() = requireNotNull(language)

}