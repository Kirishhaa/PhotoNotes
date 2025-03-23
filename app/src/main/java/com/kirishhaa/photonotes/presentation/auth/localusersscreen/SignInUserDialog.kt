package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInUserDialog(
    stateUI: SignInDialogStateUI,
    onSignIn: (SignInData) -> Unit,
    onDismiss: () -> Unit
) {
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
        modifier = Modifier.width(300.dp).height(400.dp)
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
                model = stateUI.chosenUser.picturePath,
                modifier = Modifier.size(100.dp)
            )

            Column(
                modifier = Modifier.height(260.dp).width(140.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                Text(stateUI.chosenUser.username, fontSize = 24.sp)
                TextFieldInfo("Login", error = stateUI.errorLogin, value = loginValue, onValueChanged = {
                    val valid = loginValue.length < 10
                    if(valid) loginValue = it
                })
                TextFieldInfo("Password", error = stateUI.errorPassword, value = passwordValue, onValueChanged = {
                    val valid = passwordValue.length < 10
                    if(valid) passwordValue = it
                })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().height(30.dp),
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

        if(stateUI.loading) {
            CircularProgressIndicator()
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val data = SignInData(
                            user = stateUI.chosenUser,
                            login = loginValue,
                            password = passwordValue,
                            remember = rememberState
                        )
                        onSignIn(data)
                    }
                ) {
                    Text("Sign In")
                }
            }
        }
    }
}