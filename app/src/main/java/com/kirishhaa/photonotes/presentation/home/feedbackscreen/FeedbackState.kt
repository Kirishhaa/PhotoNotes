package com.kirishhaa.photonotes.presentation.home.feedbackscreen

import com.kirishhaa.photonotes.domain.LocalUser

data class FeedbackState(
    val loadingState: Boolean = true,
    val sendingFeedback: Boolean = false,
    val user: LocalUserUI? = null
) {

    fun requireLocalUser() = requireNotNull(user)

}