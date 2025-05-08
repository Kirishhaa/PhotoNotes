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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = colorResource(R.color.secondary_container))
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
                    modifier = Modifier
                        .height(260.dp)
                        .width(140.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stateUI.chosenUser.username,
                        fontSize = 28.sp,
                        color = colorResource(R.color.primary_color),
                        fontFamily = FontFamily(Font(R.font.comic))
                    )
                    TextFieldInfo(
                        "Login",
                        error = stateUI.errorLogin,
                        value = loginValue,
                        onValueChanged = { loginValue = it })
                    TextFieldInfo(
                        "Password",
                        error = stateUI.errorPassword,
                        value = passwordValue,
                        onValueChanged = { passwordValue = it })
                }
            }
        }
        if (stateUI.loading) {
            CircularProgressIndicator(
                modifier = Modifier.offset(y = 150.dp),
                color = colorResource(R.color.primary_color)
            )
        } else {
            Row(
                modifier = Modifier.width(300.dp).offset(y = 150.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.width(135.dp).height(50.dp)
                        .pulsateClick(clickable = true, onClick = { onDismiss() })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = "Cancel",
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.width(135.dp).height(50.dp)
                        .pulsateClick(clickable = true, onClick = { onSignIn(
                            SignInData(
                                user = stateUI.chosenUser,
                                login = loginValue,
                                password = passwordValue,
                            )
                        ) })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = "Sign Up",
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}