package com.kirishhaa.photonotes.domain.language

class SelectLanguageUseCase(
    private val languageRepository: LanguageRepository
) {

    suspend fun execute(userId: Int, languageId: String) {
        languageRepository.selectLanguage(userId, languageId)
    }

}