package com.kirishhaa.photonotes.presentation.home.feedbackscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun FeedbackScreen() {
    val viewmodel: FeedbackViewModel = viewModel(factory = FeedbackViewModel.Factory)
    val state by viewmodel.state.collectAsState()

    when (state.loadingState) {
        true -> LoadingScreen()
        false -> FeedbackUserScreen(
            user = state.requireLocalUser(),
            sendingFeedback = state.sendingFeedback,
            onSend = viewmodel::send
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colorResource(R.color.primary_color)
        )
    }
}

@Composable
private fun FeedbackUserScreen(
    user: LocalUserUI,
    sendingFeedback: Boolean,
    onSend: (String, String, String) -> Unit
) {

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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.enter_a_question),
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.primary_color)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.your_name),
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.on_surface),
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = usernameValue,
            onValueChange = { usernameValue = it },
            modifier = Modifier.width(300.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = colorResource(R.color.primary_color),
                focusedIndicatorColor = colorResource(R.color.primary_color)
            )
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.question),
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.on_surface),
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = questionValue,
            onValueChange = { questionValue = it },
            modifier = Modifier
                .width(300.dp)
                .height(500.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = colorResource(R.color.primary_color),
                focusedIndicatorColor = colorResource(R.color.primary_color)
            )
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.way_to_feedback),
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.on_surface),
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = wayToFeedbackValue,
            onValueChange = { wayToFeedbackValue = it },
            modifier = Modifier.width(300.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = colorResource(R.color.primary_color),
                focusedIndicatorColor = colorResource(R.color.primary_color)
            )
        )
        Spacer(Modifier.height(30.dp))
        if (sendingFeedback) {
            CircularProgressIndicator(color = colorResource(R.color.primary_color))
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
                    .pulsateClick(clickable = true, onClick = { onSend(usernameValue, questionValue, wayToFeedbackValue) })
                    .background(
                        color = colorResource(R.color.on_secondary_container),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.send),
                    fontFamily = FontFamily(Font(R.font.comic)),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }
}