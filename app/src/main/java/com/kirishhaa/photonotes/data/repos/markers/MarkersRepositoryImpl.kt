package com.kirishhaa.photonotes.data.repos.markers

import com.kirishhaa.photonotes.data.db.dao.MarkerDao
import com.kirishhaa.photonotes.data.db.entity.FolderEntity
import com.kirishhaa.photonotes.data.db.entity.LocationEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerTagEntity
import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.Folder
import com.kirishhaa.photonotes.domain.Marker
import com.kirishhaa.photonotes.domain.exceptions.MarkerNotFoundException
import com.kirishhaa.photonotes.domain.exceptions.ReadWriteException
import com.kirishhaa.photonotes.domain.location.LocationRepository
import com.kirishhaa.photonotes.domain.markers.MarkersRepository
import com.kirishhaa.photonotes.extensions.coroutineTryCatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MarkersRepositoryImpl(
    private val markerDao: MarkerDao,
    private val locationRepository: LocationRepository,
    private val markerEntityMapper: MarkerEntityMapper,
    private val folderEntityMapper: FolderEntityMapper
) : MarkersRepository {

    override fun getMarkerById(userId: Int, markerId: Int): Flow<Marker?> {
        return combine(
            markerDao.getMarkerById(userId, markerId),
            markerDao.getMarkerLocation(markerId),
            markerDao.getMarkerTags(markerId)
        ) { markerEntity, markerLocationEntity, markerTagsEntity ->
            if (markerEntity == null || markerLocationEntity == null) return@combine null
            else {
                val folderName =
                    markerEntity.folderId?.let { folderId -> markerDao.getFolderById(folderId)?.title }
                markerEntityMapper.map(
                    markerEntity,
                    markerLocationEntity,
                    markerTagsEntity,
                    folderName
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    //throws
    //ReadWriteException
    override suspend fun createMarker(
        userId: Int,
        filePath: String
    ): Unit = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val markerEntity = MarkerEntity(
                    id = 0,
                    userId = userId,
                    folderId = null,
                    name = "Marker_${System.currentTimeMillis()}",
                    filePath = filePath,
                    description = null
                )
                markerDao.insertMarker(markerEntity)
                val entity =
                    markerDao.getMarkerByFilePath(userId, filePath) ?: throw ReadWriteException()
                val markerId = entity.id
                val location = locationRepository.getCurrentLocation() ?: DomainLocation.NONE
                val locationEntity = LocationEntity(
                    id = markerId,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    country = location.country,
                    town = location.town
                )
                markerDao.insertLocation(locationEntity)
            },
            catchBlock = { throw ReadWriteException() }
        )
    }

    //throws
    //MarkerNotFoundException
    override suspend fun getMarkerIdByFilePath(userId: Int, filePath: String): Int =
        withContext(Dispatchers.IO) {
            coroutineTryCatcher(
                tryBlock = {
                    markerDao.getMarkerByFilePath(userId, filePath)?.id
                        ?: throw MarkerNotFoundException()
                },
                catchBlock = { throwable ->
                    if (throwable !is MarkerNotFoundException) {
                        throw ReadWriteException()
                    } else {
                        throw throwable
                    }
                }
            )
        }

    override fun getAllFoldersByUserId(userId: Int): Flow<List<Folder>> {
        return markerDao.getUserFolders(userId).map { foldersEntity ->
            foldersEntity.map { folderEntity ->
                folderEntityMapper.map(folderEntity)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getAllMarkersByUserId(userId: Int): Flow<List<Marker>> {
        return markerDao.getUserMarkers(userId).map { markersEntity ->
            markersEntity.map { markerEntity ->
                val markerTagsEntity = markerDao.getMarkerTags(markerEntity.id).first()
                val markerLocationEntity = markerDao.getMarkerLocation(markerEntity.id).first()
                val folderName = markerEntity.folderId?.let { markerDao.getFolderById(it) }?.title
                markerEntityMapper.map(
                    markerEntity,
                    markerLocationEntity,
                    markerTagsEntity,
                    folderName
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    //throws
    //ReadWriteException
    override suspend fun onCreateNewFolder(folder: Folder) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val folderEntity = FolderEntity(
                    title = folder.name,
                    userId = folder.userId,
                    id = folder.id
                )
                markerDao.insertFolder(folderEntity)
            },
            catchBlock = {
                throw ReadWriteException()
            }
        )
    }

    //throws
    //ReadWriteException
    override suspend fun updateMarker(marker: Marker) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val markerEntity = markerEntityMapper.map(marker)
                val markerTagsEntity = marker.tags.map { tag ->
                    MarkerTagEntity(
                        id = tag.id,
                        markerId = markerEntity.id,
                        name = tag.name
                    )
                }
                markerDao.updateMarkerAndSetTags(markerEntity, markerTagsEntity)
            },
            catchBlock = {
                throw ReadWriteException()
            }
        )
    }

    //throws
    //MarkerNotFoundException
    //ReadWriteException
    override suspend fun removeMarkerById(markerId: Int, userId: Int) =
        withContext(Dispatchers.IO) {
            coroutineTryCatcher(
                tryBlock = {
                    val markerEntity = markerDao.getMarkerById(userId, markerId).first()
                        ?: throw MarkerNotFoundException()
                    markerDao.deleteMarker(markerEntity)
                },
                catchBlock = { throwable ->
                    if (throwable !is MarkerNotFoundException) {
                        throw ReadWriteException()
                    } else {
                        throw throwable
                    }
                }
            )
        }

    override suspend fun selectFolder(markerId: Int, userId: Int, folderId: Int?) =
        withContext(Dispatchers.IO) {
            coroutineTryCatcher(
                tryBlock = {
                    val markerEntity = markerDao.getMarkerById(userId, markerId).first()
                        ?: throw MarkerNotFoundException()
                    val newMarkerEntity = markerEntity.copy(folderId = folderId)
                    markerDao.updateMarker(newMarkerEntity)
                },
                catchBlock = { throwable ->
                    if (throwable !is MarkerNotFoundException) {
                        throw ReadWriteException()
                    } else {
                        throw throwable
                    }
                }
            )
        }

    override suspend fun removeFolder(folder: Folder) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val folderEntity = folderEntityMapper.map(folder)
                markerDao.removeFolder(folderEntity)
            },
            catchBlock = {
                throw ReadWriteException()
            }
        )
    }
}