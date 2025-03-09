package com.kirishhaa.photonotes.presentation.languagescreen

import com.kirishhaa.photonotes.domain.Language

data class LanguageState(
    val loading: Boolean = true,
    val languages: List<Language>? = null
)