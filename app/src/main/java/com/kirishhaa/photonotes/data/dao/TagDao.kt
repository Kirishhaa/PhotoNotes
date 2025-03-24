package com.kirishhaa.photonotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kirishhaa.photonotes.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM marker_tags WHERE marker_id = :markerId")
    fun getMarkerTags(markerId: Int): Flow<List<TagEntity>>

    @Update
    suspend fun updateTag(tagEntity: TagEntity)

    @Delete
    suspend fun deleteTag(tagEntity: TagEntity)

    @Insert
    suspend fun insertTag(tagEntity: TagEntity)

}