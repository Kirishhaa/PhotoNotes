package com.kirishhaa.photonotes.domain.users

class SelectPrevLanguageUseCase(
    private val localUsersRepository: LocalUsersRepository
){

    suspend fun execute(userId: Int) {
        localUsersRepository.selectPreviousLanguage(userId)
    }

}