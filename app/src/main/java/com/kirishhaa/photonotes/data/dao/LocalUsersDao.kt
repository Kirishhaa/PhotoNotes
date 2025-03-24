package com.kirishhaa.photonotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kirishhaa.photonotes.data.entity.LocalUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUsersDao {

    @Insert
    suspend fun insertUser(user: LocalUserEntity)

    @Query("SELECT * FROM local_users")
    fun getAllUsers(): Flow<List<LocalUserEntity>>

    @Update
    suspend fun updateUser(user: LocalUserEntity)

    @Delete
    suspend fun deleteUser(user: LocalUserEntity)

}