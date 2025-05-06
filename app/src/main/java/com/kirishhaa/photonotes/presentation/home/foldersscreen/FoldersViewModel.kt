package com.kirishhaa.photonotes.presentation.home.foldersscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.SingleEvent
import com.kirishhaa.photonotes.domain.Folder
import com.kirishhaa.photonotes.domain.exceptions.EnteredUserNotExistException
import com.kirishhaa.photonotes.domain.exceptions.UserNotFoundException
import com.kirishhaa.photonotes.domain.markers.CreateNewFolderUseCase
import com.kirishhaa.photonotes.domain.markers.GetAllFoldersUseCase
import com.kirishhaa.photonotes.domain.markers.GetAllMarkersUseCase
import com.kirishhaa.photonotes.domain.markers.RemoveFolderUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FoldersViewModel(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAllMarkersUseCase: GetAllMarkersUseCase,
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val createNewFolderUseCase: CreateNewFolderUseCase,
    private val removeFolderUseCase: RemoveFolderUseCase,
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
                    showEditButton = selectedFolder == null,
                    inFolder = selectedFolder != null,
                    folders = mappedFolders,
                    markers = mappedMarkers
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun selectFolder(folder: FolderUI) {
        selectedFolder.value = folder
    }

    fun handleBackPress() {
        if (selectedFolder.value != null || _state.value.showAddFolderDialog) {
            selectedFolder.value = null
            _state.value = _state.value.copy(showAddFolderDialog = false)
        } else {
            _events.tryEmit(SingleEvent(FolderEvent.CloseApp))
        }
    }

    fun showAddFolderDialog() {
        _state.value = _state.value.copy(showAddFolderDialog = true)
    }

    fun hideAddFolderDialog() {
        _state.value = _state.value.copy(showAddFolderDialog = false)
    }

    fun addNewFolder(folderName: String) {
        viewModelScope.launch {
            val enteredUser =
                getEnteredUserUseCase.execute().first() ?: throw EnteredUserNotExistException()
            val folder = Folder(
                name = folderName,
                userId = enteredUser.id
            )
            createNewFolderUseCase.execute(folder)
        }
    }

    fun onRemoveFolder() {
        viewModelScope.launch {
            val enteredUser =
                getEnteredUserUseCase.execute().first() ?: throw UserNotFoundException()
            val selectedFolder = selectedFolder.value ?: return@launch
            removeFolderUseCase.execute(enteredUser.id, selectedFolder.name)
            this@FoldersViewModel.selectedFolder.value = null
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val getEnteredUser = GetEnteredUserUseCase(app.localUsersRepository)
                val getAllMarkers = GetAllMarkersUseCase(app.markersRepository)
                val getAllFolders = GetAllFoldersUseCase(app.markersRepository)
                val markermapper = MarkerMapper()
                val foldermapper = FolderMapper()
                val createNewFolderUseCase = CreateNewFolderUseCase(app.markersRepository)
                val removeFolderUseCase = RemoveFolderUseCase(app.markersRepository)
                return FoldersViewModel(
                    getAllFolders,
                    getAllMarkers,
                    getEnteredUser,
                    createNewFolderUseCase,
                    removeFolderUseCase,
                    foldermapper,
                    markermapper
                ) as T
            }
        }
    }

}