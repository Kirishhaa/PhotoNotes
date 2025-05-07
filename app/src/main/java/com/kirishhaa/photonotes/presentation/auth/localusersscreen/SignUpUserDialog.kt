package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
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
fun SignUpUserDialog(
    state: SignUpDialogStateUI,
    onSignUp: (SignUpData) -> Unit,
    onDismiss: () -> Unit
) {
    var picturePathValue: Uri? by remember {
        mutableStateOf(null)
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        picturePathValue = uri
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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(300.dp)
                .height(390.dp)
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
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Password cannot be empty and must be in range 8..16 symbols.",
                fontFamily = FontFamily.Serif,
                color = colorResource(R.color.error_color),
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 12.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Username cannot be empty.",
                fontFamily = FontFamily.Serif,
                color = colorResource(R.color.error_color),
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 12.dp)
            )
        }

        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.offset(y = 200.dp),
                color = colorResource(R.color.primary_color)
            )
        } else {
            Row(
                modifier = Modifier.width(300.dp)
                    .offset(y = 200.dp),
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
                        .pulsateClick(clickable = true, onClick = { onSignUp(
                            SignUpData(
                                username = usernameValue,
                                login = loginValue,
                                password = passwordValue,
                                picturePath = picturePathValue,
                                remember = true
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