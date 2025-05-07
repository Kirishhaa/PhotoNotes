package com.kirishhaa.photonotes.presentation.home.camerascreen

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R
import com.kirishhaa.photonotes.clickeffects.pulsateClick

@SuppressLint("UseCheckPermission")
@Composable
fun CameraScreen(
    onNewImageCaptured: (Int) -> Unit
) {
    val viewmodel: CameraViewModel = viewModel(factory = CameraViewModel.Factory)
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when (val valueEvent = event.getValue()) {
                is CameraEvent.OnNewImageCaptured -> onNewImageCaptured(valueEvent.markerId)
                null -> {}
            }
        }
    }

    when {
        state.loadingState -> LoadingScreen()
        else -> {
            CameraScreen(
                state = state,
                onLaunchCameraPermission = { viewmodel.decreaseRequestPermissionCount(android.Manifest.permission.CAMERA) },
                onLaunchLocationPermission = { viewmodel.decreaseRequestPermissionCount(android.Manifest.permission.ACCESS_COARSE_LOCATION) },
                onImageCaptured = viewmodel::onImageCaptured
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator(
        color = colorResource(R.color.primary_color)
    ) }
}

@Composable
private fun CameraScreen(
    state: CameraState,
    onLaunchCameraPermission: () -> Unit,
    onLaunchLocationPermission: () -> Unit,
    onImageCaptured: (String) -> Unit
) {
    val context = LocalContext.current

    var cameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var locationGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        locationGranted = granted
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onLaunchLocationPermission()
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        cameraGranted = granted
    }

    var imagePath: String? by remember {
        mutableStateOf(null)
    }

    val cameralaucnher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imagePath?.let { path -> onImageCaptured(path) }
        }
    }

    val lo = LocalLifecycleOwner.current

    var resumed by remember {
        mutableStateOf(false)
    }

    DisposableEffect(lo) {
        val observer = LifecycleEventObserver { source, event ->
            resumed = source.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lo.lifecycle.addObserver(observer)
        onDispose {
            lo.lifecycle.removeObserver(observer)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.creatingNewMarker || resumed.not() -> {
                CircularProgressIndicator(color = colorResource(R.color.primary_color))
            }

            state.requestCameraPermissionCount == 0 && cameraGranted.not() -> {
                Text(
                    text = stringResource(R.string.to_take_a_photo_you_need_a_permission),
                    fontSize = 24.sp,
                    color = colorResource(R.color.on_surface),
                    fontFamily = FontFamily(Font(R.font.comic)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            resumed -> {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp)
                        .offset(y = -5.dp)
                        .pulsateClick(clickable = true, onClick = {
                            if (cameraGranted) {
                                val uri = ComposeFileProvider.getImageUri(context)
                                imagePath = uri.toString()
                                cameralaucnher.launch(uri)
                            } else {
                                onLaunchCameraPermission()
                                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        })
                        .background(
                            color = colorResource(R.color.on_secondary_container),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.take_a_photo),
                        fontFamily = FontFamily(Font(R.font.comic)),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}