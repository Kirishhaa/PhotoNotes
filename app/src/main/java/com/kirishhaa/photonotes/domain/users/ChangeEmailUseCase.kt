package com.kirishhaa.photonotes.domain.users

class ChangeEmailUseCase(
    private val usersRepository: LocalUsersRepository
) {


    suspend fun execute(
        userId: Int,
        currentEmail: String,
        newEmail: String,
        repeatNewEmail: String
    ) {
        usersRepository.changeEmail(userId, currentEmail, newEmail, repeatNewEmail)
    }

}