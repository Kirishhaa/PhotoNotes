package com.kirishhaa.photonotes.domain.feedback

sealed interface FeedbackEvent {

    data object SentMessage : FeedbackEvent

}