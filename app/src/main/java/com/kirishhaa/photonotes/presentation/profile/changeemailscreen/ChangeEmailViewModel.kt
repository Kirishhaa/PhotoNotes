package com.kirishhaa.photonotes.presentation.profile.changeemailscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.exceptions.EmailsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.WrongLoginException
import com.kirishhaa.photonotes.domain.users.ChangeEmailUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ChangeEmailViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val changeEmailUseCase: ChangeEmailUseCase
): ViewModel() {

    private val _events = Channel<ChangeEmailEvents>()
    val events = _events.receiveAsFlow()

    private val _enteredUser = MutableStateFlow<LocalUser?>(null)
    private val _state = MutableStateFlow(ChangeEmailState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when(throwable) {
            is EmailsAreNotTheSameException -> {
                _state.value = _state.value.copy(emailsNotSameError = true, currentEmailError = false, loading = false)
            }
            is WrongLoginException -> {
                _state.value = _state.value.copy(currentEmailError = true, emailsNotSameError = false, loading = false)
            }
        }
    }

    init {
        viewModelScope.launch {
            getEnteredUserUseCase.execute().collect {
                _enteredUser.value = it
                _state.value = _state.value.copy(currentEmail = it?.login ?: _state.value.currentEmail, loading = false)
            }
        }
    }

    fun onChange(
        currentEmail: String,
        newEmail: String,
        repeatNewEmail: String
    ) {
        viewModelScope.launch(exceptionHandler) {
            val enteredUser = _enteredUser.value ?: return@launch
            _state.value = _state.value.copy(loading = true)
            changeEmailUseCase.execute(enteredUser.id, currentEmail, newEmail, repeatNewEmail)
            _state.value = _state.value.copy(currentEmailError = false, emailsNotSameError = false)
            _events.trySend(ChangeEmailEvents.DataWasChanged)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val app  =extras.toApp()
                val enteredUser = GetEnteredUserUseCase(app.localUsersRepository)
                val validateAndChange = ChangeEmailUseCase(app.localUsersRepository)
                return ChangeEmailViewModel(enteredUser, validateAndChange) as T
            }
        }
    }

}