package com.kirishhaa.photonotes.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface LanguageRepository {

    fun getAllLanguages(): Flow<List<Language>>

    suspend fun selectLanguage(userId: Int, languageId: String)

    class MockImpl: LanguageRepository {

        private val languages = MutableStateFlow(
            listOf(
                Language(
                    selected = true,
                    name = "Ukrainian"
                ),
                Language(
                    selected = false,
                    name = "English"
                )
            )
        )

        override fun getAllLanguages(): Flow<List<Language>> = languages

        override suspend fun selectLanguage(userId: Int, languageId: String) = withContext(Dispatchers.IO) {
            delay(2000)
            val languages = languages.value.toMutableList()
            val selectedIndex = languages.indexOfFirst { it.selected }
            languages[selectedIndex] = Language(selected = false, name = languages[selectedIndex].name)
            val newSelectedIndex = languages.indexOfFirst { it.id == languageId }
            languages[newSelectedIndex] = Language(selected = true, name = languages[newSelectedIndex].name)
            this@MockImpl.languages.emit(languages)
        }

    }

}