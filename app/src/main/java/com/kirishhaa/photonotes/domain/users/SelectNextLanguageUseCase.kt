package com.kirishhaa.photonotes.domain.users

class SelectNextLanguageUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(userId: Int) {
        localUsersRepository.selectNextLanguage(userId)
    }

}