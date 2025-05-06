package com.kirishhaa.photonotes.data.repos.permissions

import android.content.Context
import androidx.core.content.edit
import com.kirishhaa.photonotes.domain.permissions.PermissionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PermissionsRepositoryImpl(
    appContext: Context
) : PermissionsRepository {

    private val preferences =
        appContext.getSharedPreferences("PermissionsStorage", Context.MODE_PRIVATE)

    override suspend fun decreasePermissionsCount(name: String) = withContext(Dispatchers.IO) {
        val permCount = getPermissionsCount(name)
        preferences.edit(commit = true) { putInt(name, permCount - 1) }
    }

    override suspend fun getPermissionsCount(name: String): Int = withContext(Dispatchers.IO) {
        return@withContext preferences.getInt(name, 2)
    }

}