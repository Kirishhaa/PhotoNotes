package com.kirishhaa.photonotes.domain.users

class SignInUseCase(
    private val repository: LocalUsersRepository
) {

    suspend fun execute(userId: Int, login: String, password: String, remember: Boolean) {
        repository.signIn(userId, login, password, remember)
    }

}