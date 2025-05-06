package com.kirishhaa.photonotes.domain.users

import android.net.Uri

class SignUpUseCase(
    private val repository: LocalUsersRepository
) {

    suspend fun execute(
        username: String,
        login: String,
        password: String,
        remember: Boolean,
        imagePath: Uri?
    ) {
        repository.signUp(username, login, password, remember, imagePath)
    }

}