package com.kirishhaa.photonotes.domain

interface PermissionsRepository {

    suspend fun decreasePermissionsCount(name: String)

    suspend fun getPermissionsCount(name: String): Int

    class Mockk: PermissionsRepository {

        val map = mutableMapOf(
            android.Manifest.permission.CAMERA to 2,
            android.Manifest.permission.ACCESS_COARSE_LOCATION to 2
        )

        override suspend fun decreasePermissionsCount(name: String) {
            val permCount = map[name]!!
            var newCount = permCount - 1
            if(newCount < 0) newCount = 0
            map[name] = newCount
        }

        override suspend fun getPermissionsCount(name: String): Int = map[name]!!

    }

}