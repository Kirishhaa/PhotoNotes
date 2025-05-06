package com.kirishhaa.photonotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kirishhaa.photonotes.data.db.entity.FolderEntity
import com.kirishhaa.photonotes.data.db.entity.LocationEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {

    @Query("SELECT * FROM markers WHERE user_id = :userId")
    fun getUserMarkers(userId: Int): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM markers WHERE user_id = :userId AND id = :markerId")
    fun getMarkerById(userId: Int, markerId: Int): Flow<MarkerEntity?>

    @Query("SELECT * FROM markers WHERE user_id = :userId AND file_path = :filePath")
    suspend fun getMarkerByFilePath(userId: Int, filePath: String): MarkerEntity?

    @Transaction
    suspend fun updateMarkerAndSetTags(
        markerEntity: MarkerEntity,
        markerTagsEntity: List<MarkerTagEntity>
    ) {
        clearAndInsertTags(markerEntity.id, markerTagsEntity)
        updateMarker(markerEntity)
    }

    @Update
    suspend fun updateMarker(markerEntity: MarkerEntity)

    @Delete
    suspend fun deleteMarker(markerEntity: MarkerEntity)

    @Insert
    suspend fun insertMarker(markerEntity: MarkerEntity)

    @Query("SELECT * FROM marker_tags WHERE marker_id = :markerId")
    fun getMarkerTags(markerId: Int): Flow<List<MarkerTagEntity>>

    @Transaction
    suspend fun clearAndInsertTags(markerId: Int, tags: List<MarkerTagEntity>) {
        clearTags(markerId)
        insertTags(tags)
    }

    @Insert
    suspend fun insertTags(tags: List<MarkerTagEntity>)

    @Query("DELETE FROM marker_tags WHERE marker_id = :markerId")
    suspend fun clearTags(markerId: Int)

    @Query("SELECT * FROM locations WHERE marker_id = :markerId")
    fun getMarkerLocation(markerId: Int): Flow<LocationEntity?>

    @Insert
    suspend fun insertLocation(locationEntity: LocationEntity)


    @Query("SELECT * FROM folders WHERE user_id = :userId")
    fun getUserFolders(userId: Int): Flow<List<FolderEntity>>

    @Update
    suspend fun updateFolder(folder: FolderEntity)

    @Delete
    suspend fun removeFolder(folder: FolderEntity)

    @Insert
    suspend fun insertFolder(folder: FolderEntity)

}