package com.kirishhaa.photonotes.domain.permissions

class DecreasePermissionCountUseCase(
    private val permissionsRepository: PermissionsRepository
) {

    suspend fun execute(name: String) = permissionsRepository.decreasePermissionsCount(name)

}