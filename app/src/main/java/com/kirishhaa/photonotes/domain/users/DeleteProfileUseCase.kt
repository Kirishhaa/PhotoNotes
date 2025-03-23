package com.kirishhaa.photonotes.domain.users

class DeleteProfileUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(userId: Int) {
        localUsersRepository.deleteProfile(userId)
    }

}