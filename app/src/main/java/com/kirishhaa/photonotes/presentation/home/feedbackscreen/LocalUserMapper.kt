package com.kirishhaa.photonotes.presentation.home.feedbackscreen

import com.kirishhaa.photonotes.domain.LocalUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalUserMapper {

    suspend fun map(localUser: LocalUser): LocalUserUI = withContext(Dispatchers.Default) {
        return@withContext LocalUserUI(
            id = localUser.id,
            login = localUser.login,
            name = localUser.name
        )
    }

}