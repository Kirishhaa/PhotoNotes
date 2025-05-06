package com.kirishhaa.photonotes.domain.users

import android.net.Uri

class ChangeUserPhotoUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(uri: Uri?, userId: Int) {
        localUsersRepository.changeUserPhoto(userId, uri)
    }

}