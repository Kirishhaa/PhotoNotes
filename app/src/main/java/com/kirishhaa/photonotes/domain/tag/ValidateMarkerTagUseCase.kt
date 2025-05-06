package com.kirishhaa.photonotes.domain.tag

import com.kirishhaa.photonotes.domain.exceptions.CurrentTagExistException
import com.kirishhaa.photonotes.domain.exceptions.EmptyMarkerTagException
import com.kirishhaa.photonotes.domain.exceptions.TooLargeMarkerTagLengthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ValidateMarkerTagUseCase {

    suspend fun execute(tag: String, otherTags: List<String>) = withContext(Dispatchers.Default) {
        if (tag.trim().isEmpty()) throw EmptyMarkerTagException()
        if (tag.length > 10) throw TooLargeMarkerTagLengthException()
        if (otherTags.contains(tag)) throw CurrentTagExistException()
    }

}