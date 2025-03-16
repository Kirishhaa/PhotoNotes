package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.ImagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class MarkerDetailViewModel(
    private val markerId: Int,
    private val getMarkerByIdUseCase: GetMarkerByIdUseCase
): ViewModel() {

    private val _state = MutableStateFlow<MarkerDetailState>(MarkerDetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = MarkerDetailState(
                preloading = false,
                marker = getMarkerByIdUseCase.execute(markerId)
            )
        }
    }

    companion object {
        fun Factory(markerId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val getMarker = GetMarkerByIdUseCase(ImagesRepository.Mockk)
                return MarkerDetailViewModel(markerId, getMarker) as T
            }
        }
    }

}