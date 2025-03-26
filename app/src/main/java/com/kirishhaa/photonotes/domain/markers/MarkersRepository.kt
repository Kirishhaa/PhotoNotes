package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.Folder
import com.kirishhaa.photonotes.domain.Marker
import kotlinx.coroutines.flow.Flow

interface MarkersRepository {

    fun getMarkerById(userId: Int, markerId: Int): Flow<Marker?>

    //returns marker id
    suspend fun createMarker(userId: Int, filePath: String)

    suspend fun getMarkerIdByFilePath(userId: Int, filePath: String): Int

    fun getAllFoldersByUserId(userId: Int): Flow<List<Folder>>

    fun getAllMarkersByUserId(userId: Int): Flow<List<Marker>>

    suspend fun onCreateNewFolder(folder: Folder)

    suspend fun updateMarker(marker: Marker)

    suspend fun removeMarkerById(markerId: Int, userId: Int)

}