package com.kirishhaa.photonotes.presentation.profile.profilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.users.DeleteProfileUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import com.kirishhaa.photonotes.domain.users.LogOutUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ProfileViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    private val _enteredUser = MutableStateFlow<LocalUser?>(null)
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.value = _state.value.copy(loading = false)
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            getEnteredUserUseCase.execute().collect { enteredUser ->
                _enteredUser.value = enteredUser
                _state.value = _state.value.copy(user = enteredUser, loading = false)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch(exceptionHandler) {
            val userId = _enteredUser.value?.id ?: return@launch
            _state.value = _state.value.copy(loading = true)
            deleteProfileUseCase.execute(userId)
            _events.trySend(ProfileEvent.ToAuth)
        }
    }

    fun onLogOut() {
        viewModelScope.launch(exceptionHandler) {
            val userId = _enteredUser.value?.id ?: return@launch
            _state.value = _state.value.copy(loading = true)
            logOutUseCase.execute(userId)
            _events.trySend(ProfileEvent.ToAuth)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val getEnteredUser = GetEnteredUserUseCase(app.localUsersRepository)
                val deleteProfile = DeleteProfileUseCase(app.localUsersRepository)
                val logOutUseCase = LogOutUseCase(app.localUsersRepository)
                return ProfileViewModel(getEnteredUser, deleteProfile, logOutUseCase) as T
            }
        }
    }

}