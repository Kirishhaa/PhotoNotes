package com.kirishhaa.photonotes.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface ImagesRepository {

    suspend fun getMarkerByIdUseCase(markerId: Int): Marker

    //returns marker id
    suspend fun onImageCaptured(userId: Int, filePath: String, location: DomainLocation?): Int


    object Mockk: ImagesRepository {
        private var marker: Marker? = null

        override suspend fun getMarkerByIdUseCase(markerId: Int): Marker = withContext(Dispatchers.IO) {
            delay(500)
            return@withContext marker!!
        }

        override suspend fun onImageCaptured(userId: Int, filePath: String, location: DomainLocation?): Int  = withContext(Dispatchers.IO) {
            delay(500)
            marker = Marker(32, filePath, location)
            return@withContext 0
        }

    }

}