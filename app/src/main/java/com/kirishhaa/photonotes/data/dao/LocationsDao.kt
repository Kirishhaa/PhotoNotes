package com.kirishhaa.photonotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kirishhaa.photonotes.data.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationsDao {

    @Query("SELECT * FROM locations WHERE marker_id = :markerId")
    fun getMarkerLocation(markerId: Int): Flow<LocationEntity?>

    @Insert
    suspend fun insertLocation(locationEntity: LocationEntity)

}