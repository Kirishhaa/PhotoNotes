package com.kirishhaa.photonotes.presentation.profile.profilescreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.presentation.auth.localusersscreen.UserImage

@Composable
fun ProfileScreen(
    onChangeEmail: () -> Unit,
    onChangeName: () -> Unit,
    onChangePas: () -> Unit,
    onChangeLanguage: () -> Unit,
    toAuth: () -> Unit
) {
    val viewmodel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
    val state by viewmodel.state.collectAsState()


    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when (event) {
                ProfileEvent.ToAuth -> toAuth()
            }
        }
    }

    when (val currentUser = state.user) {
        null -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        else -> ProfileScreen(
            user = currentUser,
            onChangePhoto = viewmodel::onChangePhoto,
            onChangeEmail,
            onChangeName,
            onChangePas,
            onChangeLanguage,
            viewmodel::onDelete,
            viewmodel::onLogOut
        )
    }
}

@Composable
private fun ProfileScreen(
    user: LocalUser,
    onChangePhoto: (Uri?) -> Unit,
    onChangeEmail: () -> Unit,
    onChangeName: () -> Unit,
    onChangePas: () -> Unit,
    onChangeLanguage: () -> Unit,
    onDelete: () -> Unit,
    onLogOut: () -> Unit
) {

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onChangePhoto(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserImage(
                model = user.imagePath,
                edit = true,
                onEdit = { pickPhotoLauncher.launch(PickVisualMediaRequest()) },
                modifier = Modifier.size(130.dp)
            )

            Text(
                text = user.name,
                fontSize = 32.sp,
            )
        }

        Spacer(Modifier.weight(1f))
        Button(onChangeEmail, modifier = Modifier.width(200.dp)) { Text(stringResource(R.string.change_email)) }
        Spacer(Modifier.height(8.dp))
        Button(onChangeName, modifier = Modifier.width(200.dp)) { Text(stringResource(R.string.change_username)) }
        Spacer(Modifier.height(8.dp))
        Button(onChangePas, modifier = Modifier.width(200.dp)) { Text(stringResource(R.string.change_password)) }
        Spacer(Modifier.height(8.dp))
        Button(onChangeLanguage, modifier = Modifier.width(200.dp)) { Text(stringResource(R.string.change_language)) }
        Spacer(Modifier.height(8.dp))
        Button(onDelete, modifier = Modifier.width(200.dp)) { Text(stringResource(R.string.delete_profile)) }
        Spacer(Modifier.height(8.dp))
        Button(onLogOut, modifier = Modifier.width(200.dp)) { Text(stringResource(R.string.log_out)) }
        Spacer(Modifier.height(16.dp))
    }
}