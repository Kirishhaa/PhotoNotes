package com.kirishhaa.photonotes.domain.users

class ChangePasswordUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(userId: Int, currentPassword: String, newPassword: String, repeatNewPassword: String) {
        localUsersRepository.changePassword(userId, currentPassword, newPassword, repeatNewPassword)
    }

}