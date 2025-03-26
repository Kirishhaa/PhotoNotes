package com.kirishhaa.photonotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kirishhaa.photonotes.data.db.entity.LanguageEntity
import com.kirishhaa.photonotes.data.db.entity.LocalUserEntity
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

    @Query("SELECT * FROM local_users WHERE id = :userId")
    fun getUser(userId: Int): Flow<LocalUserEntity?>

    @Query("SELECT * FROM languages")
    suspend fun getAllLanguages(): List<LanguageEntity>

    @Query("SELECT * FROM languages WHERE name = :name")
    suspend fun getLanguageByName(name: String): LanguageEntity

}