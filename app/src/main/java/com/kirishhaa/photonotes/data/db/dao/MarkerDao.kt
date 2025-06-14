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
import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.exceptions.ReadWriteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface MarkerDao {

    @Transaction
    suspend fun createMarkerTransaction(markerEntity: MarkerEntity, filePath: String, location: DomainLocation) {
        insertMarker(markerEntity)
        val entity = getMarkerByFilePath(markerEntity.userId, filePath) ?: throw ReadWriteException()
        val markerId = entity.id
        val locationEntity = LocationEntity(
            id = markerId,
            latitude = location.latitude,
            longitude = location.longitude,
            country = location.country,
            town = location.town
        )
        insertLocation(locationEntity)
    }

    @Query("SELECT * FROM photo WHERE user_id = :userId")
    fun getUserMarkers(userId: Int): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM photo WHERE user_id = :userId AND id = :markerId")
    fun getMarkerById(userId: Int, markerId: Int): Flow<MarkerEntity?>

    @Query("SELECT * FROM photo WHERE user_id = :userId AND photo_path = :filePath")
    suspend fun getMarkerByFilePath(userId: Int, filePath: String): MarkerEntity?

    @Transaction
    suspend fun updateMarkerAndSetTagsAndSetLocation(
        markerEntity: MarkerEntity,
        location: DomainLocation,
        markerTagsEntity: List<MarkerTagEntity>
    ) {
        val locationEntity = getMarkerLocation(markerEntity.id).first() ?: throw IllegalStateException()
        val newEntity = LocationEntity(
            id =locationEntity.id,
            country = location.country,
            town = location.town,
            latitude = location.latitude,
            longitude = location.longitude
        )
        clearTags(markerEntity.id)
        insertTags(markerTagsEntity)
        updateMarker(markerEntity)
        updateLocation(newEntity)
    }

    @Update
    suspend fun updateLocation(locationEntity: LocationEntity)

    @Update
    suspend fun updateMarker(markerEntity: MarkerEntity)

    @Delete
    suspend fun deleteMarker(markerEntity: MarkerEntity)

    @Insert
    suspend fun insertMarker(markerEntity: MarkerEntity)

    @Query("SELECT * FROM phototag WHERE photo_id = :markerId")
    fun getMarkerTags(markerId: Int): Flow<List<MarkerTagEntity>>

    @Transaction
    suspend fun clearAndInsertTags(markerId: Int, tags: List<MarkerTagEntity>) {
        clearTags(markerId)
        insertTags(tags)
    }

    @Insert
    suspend fun insertTags(tags: List<MarkerTagEntity>)

    @Query("DELETE FROM phototag WHERE photo_id = :markerId")
    suspend fun clearTags(markerId: Int)

    @Query("SELECT * FROM location WHERE id = :markerId")
    fun getMarkerLocation(markerId: Int): Flow<LocationEntity?>

    @Insert
    suspend fun insertLocation(locationEntity: LocationEntity)


    @Query("SELECT * FROM folder WHERE user_id = :userId")
    fun getUserFolders(userId: Int): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folder WHERE id =:folderId")
    suspend fun getFolderById(folderId: Int): FolderEntity?

    @Update
    suspend fun updateFolder(folder: FolderEntity)

    @Delete
    suspend fun removeFolder(folder: FolderEntity)

    @Insert
    suspend fun insertFolder(folder: FolderEntity)

}