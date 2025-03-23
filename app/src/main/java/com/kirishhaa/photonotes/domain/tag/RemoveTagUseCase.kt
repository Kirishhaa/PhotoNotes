package com.kirishhaa.photonotes.domain.tag

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoveTagUseCase {

    suspend fun execute(tag: String, otherTags: List<String>): List<String> = withContext(Dispatchers.Default) {
        return@withContext otherTags.toMutableList().apply {
            removeIf { otherTag -> otherTag == tag }
        }.toList()
    }

}