package com.kirishhaa.photonotes.data.repos.markers

import com.kirishhaa.photonotes.data.db.entity.FolderEntity
import com.kirishhaa.photonotes.domain.Folder

class FolderEntityMapper {

    fun map(folderEntity: FolderEntity): Folder {
        return Folder(
            name = folderEntity.name,
            userId = folderEntity.userId
        )
    }

    fun map(folder: Folder): FolderEntity {
        return FolderEntity(
            name = folder.name,
            userId = folder.userId
        )
    }

}