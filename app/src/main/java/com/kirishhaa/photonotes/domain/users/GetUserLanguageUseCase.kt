package com.kirishhaa.photonotes.domain.users

class GetUserLanguageUseCase(
    private val usersRepository: LocalUsersRepository
) {

    fun execute(userId: Int) = usersRepository.getUserLanguage(userId)

}