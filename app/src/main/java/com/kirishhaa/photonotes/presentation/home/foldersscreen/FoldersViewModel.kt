package com.kirishhaa.photonotes.presentation.home.foldersscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.SingleEvent
import com.kirishhaa.photonotes.domain.Folder
import com.kirishhaa.photonotes.domain.markers.GetAllFoldersUseCase
import com.kirishhaa.photonotes.domain.markers.GetAllMarkersUseCase
import com.kirishhaa.photonotes.domain.markers.MarkersRepository
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FoldersViewModel(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAllMarkersUseCase: GetAllMarkersUseCase,
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val folderMapper: FolderMapper,
    private val markerMapper: MarkerMapper
) : ViewModel() {

    private val _events = MutableSharedFlow<SingleEvent<FolderEvent>>(replay = 1)
    val events: Flow<SingleEvent<FolderEvent>> = _events

    private val selectedFolder = MutableStateFlow<FolderUI?>(null)
    private val _state = MutableStateFlow(FoldersState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val userId = getEnteredUserUseCase.execute().first()!!.id
            combine(
                selectedFolder,
                getAllFoldersUseCase.execute(userId),
                getAllMarkersUseCase.execute(userId)
            ) { selectedFolder, folders, markers ->
                val stateMarkers = markers.mapNotNull {
                    if (selectedFolder == null) {
                        if (it.folderName == null) it else null
                    } else {
                        if (it.folderName == selectedFolder.name) it else null
                    }
                }
                val stateFolders = if (selectedFolder == null) folders else emptyList()

                val mappedMarkers = stateMarkers.map { marker -> markerMapper.map(marker) }
                val mappedFolders = stateFolders.map { folder -> folderMapper.map(folder) }

                FoldersState(
                    loadingState = false,
                    folders = mappedFolders,
                    markers = mappedMarkers
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun selectFolder(folder: FolderUI) {
        selectedFolder.value  = folder
    }

    fun handleBackPress() {
        if(selectedFolder.value != null) {
            selectedFolder.value = null
        } else {
            _events.tryEmit(SingleEvent(FolderEvent.CloseApp))
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val getEnteredUser = GetEnteredUserUseCase(LocalUsersRepository.Mock)
                val getAllMarkers = GetAllMarkersUseCase(MarkersRepository.Mockk)
                val getAllFolders = GetAllFoldersUseCase(MarkersRepository.Mockk)
                val markermapper = MarkerMapper()
                val foldermapper = FolderMapper()
                return FoldersViewModel(getAllFolders, getAllMarkers, getEnteredUser, foldermapper, markermapper) as T
            }
        }
    }

}