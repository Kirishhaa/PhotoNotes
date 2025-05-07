package com.kirishhaa.photonotes.data.repos.feedback

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kirishhaa.photonotes.domain.feedback.FeedbackRepository

class FeedbackRepositoryImpl(
    private val context: Context
) : FeedbackRepository {

    override suspend fun sendFeedback(
        usernameValue: String,
        questionValue: String,
        wayToFeedback: String
    ) {
        val messageValue =
            "Username: $usernameValue\nQuestion: $questionValue\nWay To Feedback: $wayToFeedback"
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:"))
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("testrecipient@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        intent.putExtra(Intent.EXTRA_TEXT, messageValue)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}