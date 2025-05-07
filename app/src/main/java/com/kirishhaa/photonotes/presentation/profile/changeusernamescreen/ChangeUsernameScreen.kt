package com.kirishhaa.photonotes.presentation.profile.changeusernamescreen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R

@Composable
fun ChangeUsernameScreen() {
    val viewmodel: ChangeUsernameViewModel = viewModel(factory = ChangeUsernameViewModel.Factory)
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when (event) {
                ChangeUsernameEvent.UsernameWasChanged -> {
                    Toast.makeText(context,
                        context.getString(R.string.data_was_changed), Toast.LENGTH_SHORT).show()
                }
                ChangeUsernameEvent.ReadWrite -> {
                    Toast.makeText(context,
                        context.getString(R.string.read_write_exception), Toast.LENGTH_SHORT).show()
                }
                ChangeUsernameEvent.UserAlreadyExist -> {
                    Toast.makeText(context,
                        context.getString(R.string.user_already_exist), Toast.LENGTH_SHORT).show()
                }
                ChangeUsernameEvent.UserNotFound -> {
                    Toast.makeText(context,
                        context.getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
                }
                ChangeUsernameEvent.WrongUsername -> {
                    Toast.makeText(context,
                        context.getString(R.string.wrong_username), Toast.LENGTH_SHORT).show()
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
            ChangeUsernameScreen(state, onChange = viewmodel::changeName)
        }
    }
}

@Composable
private fun ChangeUsernameScreen(state: ChangeUsernameState, onChange: (String) -> Unit) {
    val actualUsernameValue by remember {
        mutableStateOf(state.currentUsername)
    }
    var newUsernameValue by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.change_your_username),
            fontSize = 32.sp
        )
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.actual_username), fontSize = 24.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(6.dp))
                Text(actualUsernameValue, fontSize = 24.sp, textAlign = TextAlign.Center)

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.new_username),
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = newUsernameValue,
                    onValueChange = { newUsernameValue = it },
                    modifier = Modifier.width(300.dp),
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        onChange(newUsernameValue)
                    }
                ) {
                    Text(stringResource(R.string.change))
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}