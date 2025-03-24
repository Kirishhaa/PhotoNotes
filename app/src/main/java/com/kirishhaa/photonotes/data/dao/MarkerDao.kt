package com.kirishhaa.photonotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kirishhaa.photonotes.data.entity.MarkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {

    @Query("SELECT * FROM markers WHERE user_id = :userId")
    fun getUserMarkers(userId: Int): Flow<List<MarkerEntity>>

    @Update
    suspend fun updateMarker(markerEntity: MarkerEntity)

    @Delete
    suspend fun deleteMarker(markerEntity: MarkerEntity)

    @Insert
    suspend fun insertMarker(markerEntity: MarkerEntity)

}