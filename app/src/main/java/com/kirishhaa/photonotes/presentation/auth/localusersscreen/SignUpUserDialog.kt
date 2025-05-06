package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpUserDialog(
    state: SignUpDialogStateUI,
    onSignUp: (SignUpData) -> Unit,
    onDismiss: () -> Unit
) {
    var picturePathValue by remember {
        mutableStateOf("")
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        picturePathValue = uri?.toString() ?: ""
    }

    var usernameValue by remember {
        mutableStateOf("")
    }
    var loginValue by remember {
        mutableStateOf("")
    }
    var passwordValue by remember {
        mutableStateOf("")
    }
    var rememberState by remember {
        mutableStateOf(true)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .width(300.dp)
            .height(400.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(colors = listOf(Color.Cyan, Color.Green)))
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            UserImage(
                model = picturePathValue,
                modifier = Modifier.size(100.dp),
                edit = true,
                onEdit = { pickPhotoLauncher.launch(PickVisualMediaRequest()) }
            )

            Column(
                modifier = Modifier
                    .height(260.dp)
                    .width(140.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                TextFieldInfo(
                    "Username",
                    error = state.errorUsername,
                    value = usernameValue,
                    onValueChanged = {
                        val valid = usernameValue.length < 10
                        if (valid) usernameValue = it
                    })
                TextFieldInfo(
                    "Login",
                    error = state.errorLogin,
                    value = loginValue,
                    onValueChanged = {
                        val valid = loginValue.length < 10
                        if (valid) loginValue = it
                    })
                TextFieldInfo(
                    "Password",
                    error = state.errorPassword,
                    value = passwordValue,
                    onValueChanged = {
                        val valid = passwordValue.length < 10
                        if (valid) passwordValue = it
                    })
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Remember", fontSize = 16.sp)
            Spacer(Modifier.width(30.dp))
            Checkbox(
                checked = rememberState,
                onCheckedChange = { rememberState = it },
                modifier = Modifier.size(30.dp)
            )
        }

        if (state.loading) {
            CircularProgressIndicator()
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onDismiss) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val data = SignUpData(
                            username = usernameValue,
                            login = loginValue,
                            password = passwordValue,
                            picturePath = picturePathValue,
                            remember = true
                        )
                        onSignUp(data)
                    }
                ) {
                    Text("Sign Up")
                }
            }
        }
    }

}