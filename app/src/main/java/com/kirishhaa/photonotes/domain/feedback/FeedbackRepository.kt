package com.kirishhaa.photonotes.domain.feedback

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface FeedbackRepository {

    suspend fun sendFeedback(usernameValue: String, questionValue: String, wayToFeedback: String)

    object Mockk: FeedbackRepository {

        override suspend fun sendFeedback(
            usernameValue: String,
            questionValue: String,
            wayToFeedback: String
        ) = withContext(Dispatchers.IO) {
            delay(3000)

        }

    }

}