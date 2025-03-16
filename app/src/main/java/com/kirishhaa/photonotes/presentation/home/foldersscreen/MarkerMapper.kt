package com.kirishhaa.photonotes.presentation.home.foldersscreen

import com.kirishhaa.photonotes.domain.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarkerMapper {

    suspend fun map(marker: Marker): MarkerUI = withContext(Dispatchers.Default) {
        MarkerUI(
            id = marker.id,
            imagePath = marker.filePath,
            name = marker.name
        )
    }

}