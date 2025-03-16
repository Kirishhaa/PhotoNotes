package com.kirishhaa.photonotes.domain.feedback

class SendFeedbackUseCase(
    private val feedbackRepository: FeedbackRepository
) {

    suspend fun execute(usernameValue: String, questionValue: String, wayToFeedback: String) {
        feedbackRepository.sendFeedback(usernameValue, questionValue, wayToFeedback)
    }

}