package com.kirishhaa.photonotes.presentation.home.googlemapscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.exceptions.EnteredUserNotExistException
import com.kirishhaa.photonotes.domain.markers.GetAllMarkersUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class GoogleMapViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val getAllMarkersUseCase: GetAllMarkersUseCase,
    private val markersFilter: GoogleMapMarkersFilter,
    private val markerMapper: MarkerMapper
) : ViewModel() {

    private val _state = MutableStateFlow(GoogleMapState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val enteredUser =
                getEnteredUserUseCase.execute().first() ?: throw EnteredUserNotExistException()
            getAllMarkersUseCase.execute(enteredUser.id).map { markers ->
                val filteredMarkers = markersFilter.filter(markers)
                val markersUI = filteredMarkers.map { marker -> markerMapper.map(marker) }
                GoogleMapState(markersUI)
            }.flowOn(Dispatchers.IO).collect {
                _state.value = it
            }
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val getEnteredUserUseCase = GetEnteredUserUseCase(app.localUsersRepository)
                val getAllMarkers = GetAllMarkersUseCase(app.markersRepository)
                return GoogleMapViewModel(
                    getEnteredUserUseCase,
                    getAllMarkers,
                    GoogleMapMarkersFilter(),
                    MarkerMapper(app)
                ) as T
            }
        }
    }

}