package com.kirishhaa.photonotes.domain.permissions

interface PermissionsRepository {

    suspend fun decreasePermissionsCount(name: String)

    suspend fun getPermissionsCount(name: String): Int

}