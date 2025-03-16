package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.markers.GetMarkerByIdUseCase
import com.kirishhaa.photonotes.domain.markers.MarkersRepository
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class MarkerDetailViewModel(
    private val markerId: Int,
    private val getMarkerByIdUseCase: GetMarkerByIdUseCase,
    private val getEnteredUserUseCase: GetEnteredUserUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MarkerDetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = getEnteredUserUseCase.execute().first()!!
            getMarkerByIdUseCase.execute(user.id, markerId).collect { marker ->
                _state.value = MarkerDetailState(
                    preloading = false,
                    marker = marker
                )
            }
        }
    }

    companion object {
        fun Factory(markerId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val getMarker = GetMarkerByIdUseCase(MarkersRepository.Mockk)
                val enteredUser = GetEnteredUserUseCase(LocalUsersRepository.Mock)
                return MarkerDetailViewModel(markerId, getMarker, enteredUser) as T
            }
        }
    }

}