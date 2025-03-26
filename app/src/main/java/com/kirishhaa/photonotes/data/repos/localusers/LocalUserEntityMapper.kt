package com.kirishhaa.photonotes.data.repos.localusers

import com.kirishhaa.photonotes.data.db.entity.LanguageEntity
import com.kirishhaa.photonotes.data.db.entity.LocalUserEntity
import com.kirishhaa.photonotes.domain.LocalUser

class LocalUserEntityMapper {

    fun map(localUserEntity: LocalUserEntity, languageEntity: LanguageEntity): LocalUser {
        return LocalUser(
            id = localUserEntity.id,
            imagePath = localUserEntity.imagePath,
            entered = localUserEntity.entered,
            remember = localUserEntity.remember,
            password = localUserEntity.password,
            login = localUserEntity.login,
            name = localUserEntity.name,
            language = languageEntity.toLanguage()
        )
    }

}