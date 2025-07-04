package com.kirishhaa.photonotes.data.repos.markers

import com.kirishhaa.photonotes.data.db.entity.FolderEntity
import com.kirishhaa.photonotes.domain.Folder

class FolderEntityMapper {

    fun map(folderEntity: FolderEntity): Folder {
        return Folder(
            name = folderEntity.title,
            userId = folderEntity.userId,
            id = folderEntity.id
        )
    }

    fun map(folder: Folder): FolderEntity {
        return FolderEntity(
            id = folder.id,
            title = folder.name,
            userId = folder.userId
        )
    }

}