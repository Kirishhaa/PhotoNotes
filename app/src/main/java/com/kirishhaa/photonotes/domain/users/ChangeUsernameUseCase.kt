package com.kirishhaa.photonotes.domain.users

class ChangeUsernameUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(userId: Int, newUsername: String) {
        localUsersRepository.changeUsername(userId, newUsername)
    }

}