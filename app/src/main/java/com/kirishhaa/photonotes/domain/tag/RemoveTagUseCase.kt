package com.kirishhaa.photonotes.domain.tag

import com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen.MarkerTagUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoveTagUseCase {

    suspend fun execute(tag: String, otherTags: List<MarkerTagUI>): List<MarkerTagUI> =
        withContext(Dispatchers.Default) {
            return@withContext otherTags.toMutableList().apply {
                removeIf { otherTag -> otherTag.name == tag }
            }.toList()
        }

}