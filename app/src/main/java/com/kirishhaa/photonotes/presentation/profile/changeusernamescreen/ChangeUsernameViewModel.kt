package com.kirishhaa.photonotes.presentation.profile.changeusernamescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.exceptions.PasswordsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.ReadWriteException
import com.kirishhaa.photonotes.domain.exceptions.UserAlreadyExistException
import com.kirishhaa.photonotes.domain.exceptions.UserNotFoundException
import com.kirishhaa.photonotes.domain.exceptions.WrongPasswordException
import com.kirishhaa.photonotes.domain.exceptions.WrongUsernameException
import com.kirishhaa.photonotes.domain.users.ChangeUsernameUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChangeUsernameViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val changeUsernameUseCase: ChangeUsernameUseCase
) : ViewModel() {

    private val _events = Channel<ChangeUsernameEvent>()
    val events = _events.receiveAsFlow()

    private val _enteredUser = MutableStateFlow<LocalUser?>(null)
    private val _state = MutableStateFlow(ChangeUsernameState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch(Dispatchers.IO) {
            when (throwable) {
                is UserNotFoundException -> {
                    _events.send(ChangeUsernameEvent.UserNotFound)
                }

                is UserAlreadyExistException -> {
                    _events.send(ChangeUsernameEvent.UserAlreadyExist)
                }

                is ReadWriteException -> {
                    _events.send(ChangeUsernameEvent.ReadWrite)
                }

                is WrongUsernameException -> {
                    _events.send(ChangeUsernameEvent.WrongUsername)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            getEnteredUserUseCase.execute().mapNotNull { it }.collect { enteredUser ->
                _enteredUser.value = enteredUser
                _state.value = _state.value.copy(
                    loading = false,
                    currentUsername = enteredUser.name
                )
            }
        }
    }

    fun changeName(newName: String) {
        viewModelScope.launch(exceptionHandler) {
            val user = _enteredUser.value ?: return@launch
            _state.value = _state.value.copy(loading = true)
            changeUsernameUseCase.execute(user.id, newName)
            _state.value = _state.value.copy(loading = false)
            _events.trySend(ChangeUsernameEvent.UsernameWasChanged)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val changeUsername = ChangeUsernameUseCase(app.localUsersRepository)
                val enteredUserUseCase = GetEnteredUserUseCase(app.localUsersRepository)
                return ChangeUsernameViewModel(enteredUserUseCase, changeUsername) as T
            }
        }
    }

}