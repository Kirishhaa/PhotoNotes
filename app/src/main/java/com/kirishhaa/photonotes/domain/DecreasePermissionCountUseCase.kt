package com.kirishhaa.photonotes.domain

class DecreasePermissionCountUseCase(
    private val permissionsRepository: PermissionsRepository
) {

    suspend fun execute(name: String) = permissionsRepository.decreasePermissionsCount(name)

}