package com.kirishhaa.photonotes.domain

import kotlinx.coroutines.flow.Flow

class GetLocalUsersUseCase(
    private val repository: LocalUsersRepository
) {

    fun execute(): Flow<List<LocalUser>> {
        return repository.getLocalUsers()
    }

}