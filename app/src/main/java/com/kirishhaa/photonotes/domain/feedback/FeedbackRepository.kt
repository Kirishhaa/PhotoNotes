package com.kirishhaa.photonotes.domain.feedback

interface FeedbackRepository {

    suspend fun sendFeedback(usernameValue: String, questionValue: String, wayToFeedback: String)

}