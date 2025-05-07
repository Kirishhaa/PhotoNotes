package com.kirishhaa.photonotes.presentation.markerdetail.markerdetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.exceptions.CurrentTagExistException
import com.kirishhaa.photonotes.domain.exceptions.EmptyMarkerTagException
import com.kirishhaa.photonotes.domain.exceptions.TooLargeMarkerTagLengthException
import com.kirishhaa.photonotes.domain.markers.GetAllFoldersUseCase
import com.kirishhaa.photonotes.domain.markers.GetMarkerByIdUseCase
import com.kirishhaa.photonotes.domain.markers.RemoveMarkerByIdUseCase
import com.kirishhaa.photonotes.domain.markers.SelectFolderUseCase
import com.kirishhaa.photonotes.domain.markers.UpdateMarkerUseCase
import com.kirishhaa.photonotes.domain.tag.RemoveTagUseCase
import com.kirishhaa.photonotes.domain.tag.ValidateMarkerTagUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class MarkerDetailViewModel(
    private val markerId: Int,
    private val getMarkerByIdUseCase: GetMarkerByIdUseCase,
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val validateMarkerTagUseCase: ValidateMarkerTagUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val removeTagUseCase: RemoveTagUseCase,
    private val updateMarkerUseCase: UpdateMarkerUseCase,
    private val removeMarkerByIdUseCase: RemoveMarkerByIdUseCase,
    private val selectFolderUseCase: SelectFolderUseCase,
    private val markerMapper: MarkerMapper
) : ViewModel() {

    private val _events = Channel<MarkerDetailEvent>()
    val events = _events.receiveAsFlow()

    private val tagExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is TooLargeMarkerTagLengthException -> {
                _events.trySend(MarkerDetailEvent.ShowMessage("Tag length must be <= 10"))
            }

            is EmptyMarkerTagException -> {
                _events.trySend(MarkerDetailEvent.ShowMessage("Tag cannot be empty"))
            }

            is CurrentTagExistException -> {
                _events.trySend(MarkerDetailEvent.ShowMessage("Current tag is exist"))
            }
        }
    }

    private val _state = MutableStateFlow(MarkerDetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = getEnteredUserUseCase.execute().first()!!
            combine(
                getAllFoldersUseCase.execute(user.id),
                getMarkerByIdUseCase.execute(user.id, markerId)
            ) { folders, marker ->
                MarkerDetailState(
                    preloading = false,
                    marker = marker?.let { value -> markerMapper.map(value) },
                    folders = folders.map { FolderUI(it.name, it.id) }
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun tryRemoveTag(tag: String) {
        viewModelScope.launch(tagExceptionHandler) {
            val state = _state.value
            state.marker?.let { marker ->
                val otherTags = marker.tags
                val newTags = removeTagUseCase.execute(tag, otherTags)
                _state.value = _state.value.copy(
                    marker = marker.copy(tags = newTags),
                    showRemoveTagDialog = false
                )
            }
        }
    }

    fun tryAddTag(tag: String) {
        viewModelScope.launch(tagExceptionHandler) {
            val state = _state.value
            state.marker?.let { marker ->
                val otherTags = marker.tags
                validateMarkerTagUseCase.execute(tag, otherTags)
                val newTags = marker.tags + MarkerTagUI(tag, 0)
                _state.value = _state.value.copy(
                    marker = marker.copy(tags = newTags),
                    showAddTagDialog = false
                )
            }
        }
    }

    fun showAddTagDialog(show: Boolean) {
        _state.value = _state.value.copy(showAddTagDialog = show)
    }

    fun showRemoveTagDialog(show: Boolean, tag: String?) {
        _state.value = _state.value.copy(showRemoveTagDialog = show, removeTag = tag)
    }

    fun setEditMode(edit: Boolean) {
        _state.value = _state.value.copy(editing = edit)
    }

    fun onSave(
        markerName: String,
        markerCountry: String,
        markerTown: String,
        markerDescription: String
    ) {
        viewModelScope.launch {
            val markerUI = _state.value.marker ?: return@launch
            val newMarker = markerUI.copy(
                name = markerName,
                location = markerUI.location.copy(
                    country = markerCountry,
                    town = markerTown
                ),
                description = markerDescription,
            )
            val domainMarker = markerMapper.map(newMarker)
            updateMarkerUseCase.execute(domainMarker)
            selectFolderUseCase.execute(markerId, markerUI.userId, _state.value.marker?.folderId)
            _events.trySend(MarkerDetailEvent.GoBack)
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            val marker = _state.value.marker ?: return@launch
            _state.value = _state.value.copy(preloading = true)
            removeMarkerByIdUseCase.execute(markerId, marker.userId)
            _events.trySend(MarkerDetailEvent.GoBack)
        }
    }

    fun onSelectFolder(folderId: Int) {
        viewModelScope.launch {
            getEnteredUserUseCase.execute().first()?.id?.let { userId ->
                val name =
                    getAllFoldersUseCase.execute(userId).first().find { it.id == folderId }?.name
                val newMarker = _state.value.marker?.copy(folderId = folderId, folderName = name)
                _state.value = _state.value.copy(marker = newMarker)
            }
        }
    }

    fun onRemoveFromFolder() {
        val newMarker = _state.value.marker?.copy(folderId = null, folderName = null)
        _state.value = _state.value.copy(marker = newMarker)
    }

    companion object {
        fun Factory(markerId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val getMarker = GetMarkerByIdUseCase(app.markersRepository)
                val enteredUser = GetEnteredUserUseCase(app.localUsersRepository)
                val validateTag = ValidateMarkerTagUseCase()
                val removeTag = RemoveTagUseCase()
                val updateMarkerUseCase = UpdateMarkerUseCase(app.markersRepository)
                val removeMarkerUseCase = RemoveMarkerByIdUseCase(app.markersRepository)
                val getAllFoldersUseCase = GetAllFoldersUseCase(app.markersRepository)
                val selectFolderUseCase = SelectFolderUseCase(app.markersRepository)
                val mapper = MarkerMapper()
                return MarkerDetailViewModel(
                    markerId,
                    getMarker,
                    enteredUser,
                    validateTag,
                    getAllFoldersUseCase,
                    removeTag,
                    updateMarkerUseCase,
                    removeMarkerUseCase,
                    selectFolderUseCase,
                    mapper
                ) as T
            }
        }
    }

}