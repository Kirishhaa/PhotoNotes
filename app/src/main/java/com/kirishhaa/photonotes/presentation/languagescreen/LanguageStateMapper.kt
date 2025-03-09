package com.kirishhaa.photonotes.presentation.languagescreen

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LanguageStateMapper {

    suspend fun map(innerState: LanguageState): LanguageStateUI = withContext(Dispatchers.Default) {
        val selectedLanguage = innerState.languages?.first { it.selected }
        val languageUI = if(selectedLanguage == null) null else LanguageUI.fromLanguage(selectedLanguage)
        return@withContext LanguageStateUI(
            loading = innerState.loading,
            chosenLanguage = languageUI
        )
    }

}