package com.kirishhaa.photonotes.presentation.profile.changepasswordscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@Composable
fun ChangePasswordScreen() {
    val viewmodel: ChangePasswordViewModel = viewModel(factory = ChangePasswordViewModel.Factory)
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when (event) {
                is ChangePasswordEvent.PasswordChanged -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_was_changed), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    when (state.loading) {
        true -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(R.color.primary_color))
            }
        }

        false -> {
            ChangePasswordScreen(state, viewmodel::onChange)
        }
    }
}

@Composable
private fun ChangePasswordScreen(
    state: ChangePasswordState,
    onChange: (String, String, String) -> Unit
) {
    var currentPasswordValue by remember {
        mutableStateOf("")
    }
    var newPasswordValue by remember {
        mutableStateOf("")
    }
    var repeatNewPasswordValue by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.change_your_password),
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.comic)),
            color = colorResource(R.color.on_surface)
        )
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.current_password),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.comic)),
                    color = colorResource(R.color.on_surface)
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = currentPasswordValue,
                    onValueChange = { currentPasswordValue = it },
                    modifier = Modifier.width(300.dp),
                    isError = state.currentPasswordError,
                    colors = TextFieldDefaults.colors(
                        cursorColor = colorResource(R.color.primary_color),
                        focusedIndicatorColor = colorResource(R.color.primary_color)
                    )
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.new_password),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.comic)),
                    color = colorResource(R.color.on_surface)
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = newPasswordValue,
                    onValueChange = { newPasswordValue = it },
                    modifier = Modifier.width(300.dp),
                    isError = state.passwordsNotSameError,
                    colors = TextFieldDefaults.colors(
                        cursorColor = colorResource(R.color.primary_color),
                        focusedIndicatorColor = colorResource(R.color.primary_color)
                    )
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.repeat_new_password),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.comic)),
                    color = colorResource(R.color.on_surface)
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = repeatNewPasswordValue,
                    onValueChange = { repeatNewPasswordValue = it },
                    modifier = Modifier.width(300.dp),
                    isError = state.passwordsNotSameError,
                    colors = TextFieldDefaults.colors(
                        cursorColor = colorResource(R.color.primary_color),
                        focusedIndicatorColor = colorResource(R.color.primary_color)
                    )
                )
                Spacer(Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .pulsateClick(clickable = true, onClick = {
                            onChange(currentPasswordValue, newPasswordValue, repeatNewPasswordValue)
                        }
                        )
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.change),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

        }

    }
}