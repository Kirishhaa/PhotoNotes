package com.kirishhaa.photonotes.data.repos.feedback

import com.kirishhaa.photonotes.domain.feedback.FeedbackRepository
import kotlinx.coroutines.delay

class FeedbackRepositoryImpl : FeedbackRepository {

    override suspend fun sendFeedback(
        usernameValue: String,
        questionValue: String,
        wayToFeedback: String
    ) {
        //simulate sending
        delay(1500)
    }

}