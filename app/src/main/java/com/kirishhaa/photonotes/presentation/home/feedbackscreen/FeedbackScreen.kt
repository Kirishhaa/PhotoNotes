package com.kirishhaa.photonotes.presentation.home.feedbackscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.feedback.FeedbackEvent

@Composable
fun FeedbackScreen() {
    val viewmodel: FeedbackViewModel = viewModel(factory = FeedbackViewModel.Factory)
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when(event) {
                FeedbackEvent.SentMessage -> {
                    Toast.makeText(context, "Your question was sent", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    when(state.loadingState) {
        true -> LoadingScreen()
        false -> FeedbackUserScreen(user = state.requireLocalUser(), sendingFeedback = state.sendingFeedback, onSend = viewmodel::send)
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FeedbackUserScreen(user: LocalUserUI, sendingFeedback: Boolean, onSend: (String, String, String) -> Unit) {

    var usernameValue by remember {
        mutableStateOf(user.name)
    }

    var questionValue by remember {
        mutableStateOf("")
    }

    var wayToFeedbackValue by remember {
        mutableStateOf(user.login)
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter a question", fontSize = 32.sp)
        Spacer(Modifier.height(20.dp))
        Text("Your name")
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = usernameValue,
            onValueChange = { usernameValue = it },
            modifier = Modifier.width(300.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text("Question")
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = questionValue,
            onValueChange = { questionValue = it },
            modifier = Modifier.width(300.dp).height(500.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text("Way to feedback")
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = wayToFeedbackValue,
            onValueChange = { wayToFeedbackValue = it },
            modifier = Modifier.width(300.dp)
        )

        Spacer(Modifier.height(30.dp))
        if (sendingFeedback) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onSend(usernameValue, questionValue, wayToFeedbackValue) }
            ) {
                Text("Send")
            }
        }
    }
}