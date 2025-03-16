package com.kirishhaa.photonotes.domain.language

import com.kirishhaa.photonotes.domain.Language
import kotlinx.coroutines.flow.Flow

class GetLanguagesUseCase(
    private val languageRepository: LanguageRepository
) {

    fun execute(): Flow<List<Language>> = languageRepository.getAllLanguages()

}