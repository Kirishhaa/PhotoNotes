package com.kirishhaa.photonotes.domain.markers

import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.Folder
import com.kirishhaa.photonotes.domain.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

interface MarkersRepository {

    fun getMarkerById(userId: Int, markerId: Int): Flow<Marker?>

    //returns marker id
    suspend fun onImageCaptured(userId: Int, filePath: String, location: DomainLocation?): Int

    fun getAllFoldersByUserId(userId: Int): Flow<List<Folder>>

    fun getAllMarkersByUserId(userId: Int): Flow<List<Marker>>

    suspend fun onCreateNewFolder(folder: Folder)

    suspend fun updateMarker(marker: Marker)

    suspend fun removeMarkerById(markerId: Int, userId: Int)

    object Mockk: MarkersRepository {

        private val markers = MutableStateFlow<List<Marker>>(emptyList())

        private val folders = MutableStateFlow<List<Folder>>(emptyList())

        private val idAppender = AtomicInteger(0)

        override fun getMarkerById(userId: Int, markerId: Int): Flow<Marker?> {
           return markers.map { list ->  list.firstOrNull { it.id == markerId } }
        }

        override suspend fun onImageCaptured(userId: Int, filePath: String, location: DomainLocation?): Int  = withContext(Dispatchers.IO) {
            delay(500)
            val marker = Marker(
                id = idAppender.getAndIncrement(),
                folderName = null,
                name = "Marker",
                userId = userId,
                filePath = filePath,
                location = location,
                saved = false,
                tags = emptyList(),
                description = "DESCRIPTION"
            )
            val markers = markers.value.toMutableList()
            markers += marker
            this@Mockk.markers.value = markers
            return@withContext marker.id
        }

        override fun getAllFoldersByUserId(userId: Int): Flow<List<Folder>> {
            return folders.map { list -> list.mapNotNull { folder -> if(folder.userId == userId) folder else null } }
        }

        override fun getAllMarkersByUserId(userId: Int): Flow<List<Marker>> {
            return markers.map { list -> list.mapNotNull { marker -> if(marker.userId == userId) marker else null } }
        }

        override suspend fun onCreateNewFolder(folder: Folder) = withContext(Dispatchers.IO) {
            delay(500)
            val folders = folders.value.toMutableList()
            folders += folder
            this@Mockk.folders.value = folders
        }

        override suspend fun updateMarker(marker: Marker) = withContext(Dispatchers.IO) {
            delay(500)
            val index = markers.value.indexOfFirst { it.id == marker.id }
            if(index != -1) {
                val newMarkers = markers.value.toMutableList()
                newMarkers[index] = marker
                markers.value = newMarkers
            }
        }

        override suspend fun removeMarkerById(markerId: Int, userId: Int) {
            markers.value = markers.value.toMutableList().apply {
                removeIf { it.id == markerId }
            }
        }

    }

}