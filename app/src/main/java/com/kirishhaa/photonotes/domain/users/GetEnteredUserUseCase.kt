package com.kirishhaa.photonotes.domain.users

import com.kirishhaa.photonotes.domain.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetEnteredUserUseCase(
    private val localUsersRepository: LocalUsersRepository
) {

    fun execute(): Flow<LocalUser?> = localUsersRepository.getEnteredUser()

}