package com.kirishhaa.photonotes.presentation.localusersscreen

import com.kirishhaa.photonotes.domain.LocalUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalUserMapper {

    suspend fun map(localUser: LocalUser): LocalUserUI = withContext(Dispatchers.Default) {
        return@withContext LocalUserUI(
            id = localUser.id,
            picturePath = localUser.imagePath,
            username = localUser.name
        )
    }

}