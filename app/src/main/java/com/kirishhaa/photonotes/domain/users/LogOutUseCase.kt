package com.kirishhaa.photonotes.domain.users

class LogOutUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(userId: Int) {
        localUsersRepository.logOut(userId)
    }

}