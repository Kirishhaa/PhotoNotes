package com.kirishhaa.photonotes.presentation.home.camerascreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("UseCheckPermission")
@Composable
fun CameraScreen(
    onNewImageCaptured: (Int) -> Unit
) {
    val viewmodel: CameraViewModel = viewModel(factory = CameraViewModel.Factory)
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(0) {
        viewmodel.events.collect { event ->
            when(val valueEvent = event.getValue()) {
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
                onImageCaptured = viewmodel::onImageCaptured)
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
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
        mutableStateOf(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    var locationGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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
        if(success) {
            imagePath?.let { path -> onImageCaptured(path) }
        }
    }

    val lo = LocalLifecycleOwner.current

    var resumed by remember {
        mutableStateOf(false)
    }

    DisposableEffect(lo) {
        val observer = LifecycleEventObserver { source, event ->
            if(source.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                resumed = true
            } else {
                resumed = false
            }
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
                CircularProgressIndicator()
            }
            state.requestCameraPermissionCount == 0 && cameraGranted.not() -> {
                Text("To take a photo you need a permission")
            }
            resumed -> {
                Button(
                    onClick = {
                        if(cameraGranted) {
                            val uri = ComposeFileProvider.getImageUri(context)
                            imagePath = uri.toString()
                            cameralaucnher.launch(uri)
                        } else {
                            onLaunchCameraPermission()
                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Text("Take a photo")
                }
            }
        }
    }
}