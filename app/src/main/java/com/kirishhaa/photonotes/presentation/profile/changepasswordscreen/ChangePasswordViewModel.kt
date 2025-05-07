package com.kirishhaa.photonotes.presentation.profile.changepasswordscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.exceptions.PasswordsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.WrongPasswordException
import com.kirishhaa.photonotes.domain.users.ChangePasswordUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ChangePasswordViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _events = Channel<ChangePasswordEvent>()
    val events = _events.receiveAsFlow()

    private val _enteredUser = MutableStateFlow<LocalUser?>(null)
    private val _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is PasswordsAreNotTheSameException -> {
                _state.value = _state.value.copy(
                    passwordsNotSameError = true,
                    currentPasswordError = false,
                    loading = false
                )
            }

            is WrongPasswordException -> {
                _state.value = _state.value.copy(
                    currentPasswordError = true,
                    passwordsNotSameError = false,
                    loading = false
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            getEnteredUserUseCase.execute().collect {
                _enteredUser.value = it
                _state.value = _state.value.copy(loading = false)
            }
        }
    }

    fun onChange(
        currentPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ) {
        viewModelScope.launch(exceptionHandler) {
            val enteredUser = _enteredUser.value ?: return@launch
            _state.value = _state.value.copy(loading = true)
            changePasswordUseCase.execute(
                enteredUser.id,
                currentPassword,
                newPassword,
                repeatNewPassword
            )
            _state.value =
                _state.value.copy(currentPasswordError = false, passwordsNotSameError = false)
            _events.trySend(ChangePasswordEvent.PasswordChanged)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val enteredUser = GetEnteredUserUseCase(app.localUsersRepository)
                val changePassword = ChangePasswordUseCase(app.localUsersRepository)
                return ChangePasswordViewModel(enteredUser, changePassword) as T
            }
        }
    }

}