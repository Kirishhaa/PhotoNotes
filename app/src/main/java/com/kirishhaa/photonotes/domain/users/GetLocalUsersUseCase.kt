package com.kirishhaa.photonotes.domain.users

import com.kirishhaa.photonotes.domain.LocalUser
import kotlinx.coroutines.flow.Flow

class GetLocalUsersUseCase(
    private val repository: LocalUsersRepository
) {

    fun execute(): Flow<List<LocalUser>> {
        return repository.getLocalUsers()
    }

}