package com.kirishhaa.photonotes.domain.users

import com.kirishhaa.photonotes.domain.LocalUser
import kotlinx.coroutines.flow.first

class GetEnteredUserUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    suspend fun execute(): LocalUser? = localUsersRepository.getEnteredUser().first()

}