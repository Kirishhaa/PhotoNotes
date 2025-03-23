package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.SingleEvent
import com.kirishhaa.photonotes.domain.users.GetLocalUsersUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import com.kirishhaa.photonotes.domain.users.SignInUseCase
import com.kirishhaa.photonotes.domain.users.SignUpUseCase
import com.kirishhaa.photonotes.domain.exceptions.WrongLoginException
import com.kirishhaa.photonotes.domain.exceptions.WrongPasswordException
import com.kirishhaa.photonotes.domain.exceptions.WrongUsernameException
import com.kirishhaa.photonotes.extensions.coroutineTryCatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalUsersViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getLocalUsersUseCase: GetLocalUsersUseCase,
    private val localUserMapper: LocalUserMapper
): ViewModel() {

    private val _events = MutableSharedFlow<SingleEvent<LocalUsersEvent>>(replay = 1)
    val events: Flow<SingleEvent<LocalUsersEvent>> = _events

    private val _state = MutableStateFlow(LocalUsersStateUI())
    val state: StateFlow<LocalUsersStateUI> = _state

    init {
        viewModelScope.launch {
            getLocalUsersUseCase.execute().collect { users ->
                delay(500)
                ensureActive()
                val enteredUser = users.firstOrNull { it.entered }
                if(enteredUser != null) {
                    onEnterUserExist()
                    cancel()
                } else {
                    val oldScreenState = _state.value.screenState
                    val newUsers = users.map { user -> localUserMapper.map(user) }
                    val newScreenState = oldScreenState.copy(users = newUsers, loading = false)
                    val newState = _state.value.copy(screenState = newScreenState)
                    _state.value = newState
                }
            }
        }
    }

    fun hideSignInDialog() {
        _state.value = _state.value.copy(signInDialogState = null)
    }

    fun hideSignUpDialog() {
        _state.value = _state.value.copy(signUpDialogState = null)
    }

    fun showSignInDialog(user: LocalUserUI) {
        if(isDialogShowing()) return
        val signInState = SignInDialogStateUI(
            chosenUser = user,
            loading = false,
            errorLogin = false,
            errorPassword = false
        )
        _state.value = _state.value.copy(signInDialogState = signInState, signUpDialogState = null)
    }

    fun onEdit() {
        if(isDialogShowing()) return
        val signUpState = SignUpDialogStateUI(
            loading = false,
            errorPassword = false,
            errorLogin = false,
            errorUsername = false
        )
        _state.value = _state.value.copy(signUpDialogState = signUpState, signInDialogState = null)
    }

    fun onSignIn(data: SignInData)  {
        viewModelScope.launch {
            val signInInitialState = SignInDialogStateUI(
                loading = true,
                chosenUser = data.user,
                errorPassword = false,
                errorLogin = false
            )
            _state.value = _state.value.copy(signInDialogState = signInInitialState)
            coroutineTryCatcher(
                tryBlock = {
                    signInUseCase.execute(
                        userId = data.user.id,
                        login = data.login,
                        password = data.password,
                        remember = data.remember
                    )
                },
                catchBlock = { exception ->
                    if(isShowingSignInDialog()) {
                        val errorPassword = exception is WrongPasswordException
                        val errorLogin = exception is WrongLoginException
                        val signInFinalState = SignInDialogStateUI(
                            loading = false,
                            chosenUser = data.user,
                            errorPassword = errorPassword,
                            errorLogin = errorLogin
                        )
                        _state.value = _state.value.copy(signInDialogState = signInFinalState)
                        when {
                            errorLogin -> emitEvent(LocalUsersEvent.WrongLoginException)
                            errorPassword -> emitEvent(LocalUsersEvent.WrongPasswordException)
                        }
                    }
                }
            )
        }
    }

    fun onSignUp(data: SignUpData) {
        viewModelScope.launch {
            val signUpInitialState = SignUpDialogStateUI(
                loading = true,
                errorPassword = false,
                errorLogin = false,
                errorUsername = false
            )
            _state.value = _state.value.copy(signUpDialogState = signUpInitialState)
            coroutineTryCatcher(
                tryBlock = {
                    signUpUseCase.execute(
                        username = data.username,
                        login = data.login,
                        password = data.password,
                        remember = data.remember,
                        imagePath = data.picturePath
                    )
                },
                catchBlock = { exception ->
                    if(isShowingSignUpDialog()) {
                        val errorPassword = exception is WrongPasswordException
                        val errorLogin = exception is WrongLoginException
                        val errorUsername = exception is WrongUsernameException
                        val signUpFinalState = SignUpDialogStateUI(
                            loading = false,
                            errorPassword = errorPassword,
                            errorLogin = errorLogin,
                            errorUsername = errorUsername,
                        )
                        _state.value = _state.value.copy(signUpDialogState = signUpFinalState)
                        when {
                            errorPassword -> emitEvent(LocalUsersEvent.WrongPasswordException)
                            errorLogin -> emitEvent(LocalUsersEvent.WrongLoginException)
                            errorUsername -> emitEvent(LocalUsersEvent.WrongUsernameException)
                        }
                    }
                }
            )
        }
    }

    fun handleBackNavigation() {
        viewModelScope.launch {
            if (isDialogShowing().not()) {
                emitEvent(LocalUsersEvent.CloseApp)
            } else {
                if(_state.value.signInDialogState?.loading != true && _state.value.signUpDialogState?.loading != true) {
                    _state.value = _state.value.copy(signInDialogState = null, signUpDialogState = null)
                }
            }
        }
    }

    private fun isDialogShowing(): Boolean = isShowingSignInDialog() || isShowingSignUpDialog()

    private fun isShowingSignInDialog(): Boolean = _state.value.signInDialogState != null

    private fun isShowingSignUpDialog(): Boolean = _state.value.signUpDialogState != null

    private suspend fun onEnterUserExist() {
        emitEvent(LocalUsersEvent.EnteredUserExist)
    }

    private suspend fun emitEvent(event: LocalUsersEvent) = withContext(Dispatchers.IO) {
        _events.emit(SingleEvent(event))
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val repository = LocalUsersRepository.Mock
                return LocalUsersViewModel(
                    signInUseCase = SignInUseCase(repository),
                    signUpUseCase = SignUpUseCase(repository),
                    getLocalUsersUseCase = GetLocalUsersUseCase(repository),
                    localUserMapper = LocalUserMapper()
                ) as T
            }
        }
    }

}