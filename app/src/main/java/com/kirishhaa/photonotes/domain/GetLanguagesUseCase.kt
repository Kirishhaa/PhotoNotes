package com.kirishhaa.photonotes.domain

import kotlinx.coroutines.flow.Flow

class GetLanguagesUseCase(
    private val languageRepository: LanguageRepository
) {

    suspend fun execute(): Flow<List<Language>> = languageRepository.getAllLanguages()

}