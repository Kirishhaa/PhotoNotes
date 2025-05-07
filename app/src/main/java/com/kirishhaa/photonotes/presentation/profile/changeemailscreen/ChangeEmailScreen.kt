package com.kirishhaa.photonotes.presentation.profile.changeemailscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R

@Composable
fun ChangeEmailScreen() {
    val viewmodel: ChangeEmailViewModel = viewModel(factory = ChangeEmailViewModel.Factory)
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when (event) {
                ChangeEmailEvents.EmailChanged -> {
                    Toast.makeText(context,
                        context.getString(R.string.email_was_changed), Toast.LENGTH_SHORT).show()
                }
                ChangeEmailEvents.ReadWrite -> {
                    Toast.makeText(context, context.getString(R.string.read_write_exception), Toast.LENGTH_SHORT).show()
                }
                ChangeEmailEvents.UserNotFound -> {
                    Toast.makeText(context, context.getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
                }
                ChangeEmailEvents.WrongEmail -> {
                    Toast.makeText(context,
                        context.getString(R.string.wrong_email), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    when (state.loading) {
        true -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        false -> {
            ChangeEmailScreen(state, viewmodel::onChange)
        }
    }
}

@Composable
private fun ChangeEmailScreen(state: ChangeEmailState, onChange: (String, String, String) -> Unit) {
    var currentEmailValue by remember {
        mutableStateOf(state.currentEmail)
    }
    var newEmailValue by remember {
        mutableStateOf("")
    }
    var repeatNewEmailValue by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.change_your_email),
            fontSize = 32.sp
        )
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.current_email),
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = currentEmailValue,
                    onValueChange = { currentEmailValue = it },
                    modifier = Modifier.width(300.dp),
                    isError = state.currentEmailError
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.new_email),
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = newEmailValue,
                    onValueChange = { newEmailValue = it },
                    modifier = Modifier.width(300.dp),
                    isError = state.emailsNotSameError
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.repeat_new_email),
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = repeatNewEmailValue,
                    onValueChange = { repeatNewEmailValue = it },
                    modifier = Modifier.width(300.dp),
                    isError = state.emailsNotSameError
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        onChange(currentEmailValue, newEmailValue, repeatNewEmailValue)
                    }
                ) {
                    Text(stringResource(R.string.change))
                }
                Spacer(Modifier.height(16.dp))
            }

        }

    }
}