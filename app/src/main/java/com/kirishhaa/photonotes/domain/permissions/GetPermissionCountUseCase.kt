package com.kirishhaa.photonotes.domain.permissions

class GetPermissionCountUseCase(
    private val permissionsRepository: PermissionsRepository
) {

    suspend fun execute(name: String): Int = permissionsRepository.getPermissionsCount(name)

}