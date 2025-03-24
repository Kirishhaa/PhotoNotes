package com.kirishhaa.photonotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kirishhaa.photonotes.data.entity.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoldersDao {

    @Query("SELECT * FROM folders WHERE userId = :userId")
    fun getUserFolders(userId: Int): Flow<List<FolderEntity>>

    @Update
    suspend fun updateFolder(folder: FolderEntity)

    @Delete
    suspend fun removeFolder(folder: FolderEntity)

    @Insert
    suspend fun insertFolder(folder: FolderEntity)

}