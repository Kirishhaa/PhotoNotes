package com.kirishhaa.photonotes.presentation.home.camerascreen

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.SingleEvent
import com.kirishhaa.photonotes.domain.permissions.DecreasePermissionCountUseCase
import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.location.GetLocationUseCase
import com.kirishhaa.photonotes.domain.permissions.GetPermissionCountUseCase
import com.kirishhaa.photonotes.domain.markers.ImageCapturedUseCase
import com.kirishhaa.photonotes.domain.markers.MarkersRepository
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import com.kirishhaa.photonotes.domain.location.LocationRepository
import com.kirishhaa.photonotes.domain.permissions.PermissionsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val imageCapturedUseCase: ImageCapturedUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val decreasePermissionCountUseCase: DecreasePermissionCountUseCase,
    private val getPermissionCountUseCase: GetPermissionCountUseCase
): ViewModel() {

    private val _events = MutableSharedFlow<SingleEvent<CameraEvent>>(replay = 1)
    val events: Flow<SingleEvent<CameraEvent>> = _events

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = CameraState(false)
    }

    init {
        viewModelScope.launch {
            val cameraRequestPermissionCount = getPermissionCountUseCase.execute(android.Manifest.permission.CAMERA)
            val locationRequestPermissionCount = getPermissionCountUseCase.execute(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            _state.value = CameraState(
                loadingState = false,
                creatingNewMarker = false,
                requestCameraPermissionCount = cameraRequestPermissionCount,
                requestLocationPermissionCount = locationRequestPermissionCount
            )
        }
    }

    fun decreaseRequestPermissionCount(name: String) {
        viewModelScope.launch {
            decreasePermissionCountUseCase.execute(name)
            val count = getPermissionCountUseCase.execute(name)
            if(name == android.Manifest.permission.CAMERA) {
                _state.value = _state.value.copy(
                    requestCameraPermissionCount = count
                )
            } else if(name == android.Manifest.permission.ACCESS_COARSE_LOCATION) {
                _state.value = _state.value.copy(
                    requestLocationPermissionCount = count
                )
            }
        }
    }

    fun onImageCaptured(filePath: String) {
        viewModelScope.launch(exceptionHandler) {
            _state.value = CameraState(true)
            val location: DomainLocation? = getLocationUseCase.execute()
            val markerId = imageCapturedUseCase.execute(filePath, location)
            _events.emit(SingleEvent(CameraEvent.OnNewImageCaptured(markerId)))
            _state.value = CameraState(false)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY] as Application
                val repoUsers = LocalUsersRepository.Mock
                val permsRepo = PermissionsRepository.Mockk
                val repoImages = MarkersRepository.Mockk
                val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val locationRepository = LocationRepository.Mockk(locationManager, application)
                val usecase = ImageCapturedUseCase(repoUsers, repoImages)
                val locationUseCase = GetLocationUseCase(locationRepository)
                val decreaseUseCase = DecreasePermissionCountUseCase(permsRepo)
                val getNumUseCase = GetPermissionCountUseCase(permsRepo)
                return CameraViewModel(usecase, locationUseCase, decreaseUseCase, getNumUseCase) as T
            }
        }
    }

}