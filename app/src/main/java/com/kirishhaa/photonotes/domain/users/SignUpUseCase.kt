package com.kirishhaa.photonotes.domain.users

class SignUpUseCase(
    private val repository: LocalUsersRepository
) {

    suspend fun execute(username: String, login: String, password: String, remember: Boolean) {
        repository.signUp(username, login, password, remember)
    }

}