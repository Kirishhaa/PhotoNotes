package com.kirishhaa.photonotes.presentation.languagescreen

class LanguageStateUI(
    val loading: Boolean = true,
    val chosenLanguage: LanguageUI? = null,
) {

    fun requireChosenLanguage(): LanguageUI = requireNotNull(chosenLanguage)
}