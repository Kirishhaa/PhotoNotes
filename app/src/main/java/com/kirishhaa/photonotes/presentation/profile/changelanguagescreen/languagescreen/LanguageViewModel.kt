package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.domain.users.GetUserLanguageUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import com.kirishhaa.photonotes.domain.users.SelectNextLanguageUseCase
import com.kirishhaa.photonotes.domain.users.SelectPrevLanguageUseCase
import com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen.exceptions.LanguagesAreNotExistException
import com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen.exceptions.SelectedLanguageNotFoundException
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val getUserLanguageUseCase: GetUserLanguageUseCase,
    private val selectNextLanguageUseCase: SelectNextLanguageUseCase,
    private val selectPrevLanguageUseCase: SelectPrevLanguageUseCase,
    private val languageMapper: LanguageMapper
): ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when(throwable) {
            is LanguagesAreNotExistException -> {

            }
            is SelectedLanguageNotFoundException -> {

            }
        }
    }

    private val _state = MutableStateFlow(LanguageState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val id = getEnteredUserUseCase.execute().first()?.id ?: return@launch
            getUserLanguageUseCase.execute(id).collect { lang ->
                if(lang != null)
                _state.value = _state.value.copy(loading = false, language = languageMapper.map(lang))
            }
        }
    }

    fun onNextLanguage() = changeStateOperation {
        getEnteredUserUseCase.execute().first()?.id?.let { id ->
            selectNextLanguageUseCase.execute(id)
        }
    }

    fun onPreviousLanguage() = changeStateOperation {
        getEnteredUserUseCase.execute().first()?.id?.let { id ->
            selectPrevLanguageUseCase.execute(id)
        }
    }

    private fun changeStateOperation(block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            _state.value = _state.value.copy(loading = true)
            block()
            _state.value = _state.value.copy(loading = false)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                return LanguageViewModel(
                    getUserLanguageUseCase = GetUserLanguageUseCase(app.localUsersRepository),
                    selectNextLanguageUseCase = SelectNextLanguageUseCase(app.localUsersRepository),
                    selectPrevLanguageUseCase = SelectPrevLanguageUseCase(app.localUsersRepository),
                    getEnteredUserUseCase = GetEnteredUserUseCase(app.localUsersRepository),
                    languageMapper = LanguageMapper()
                ) as T
            }
        }
    }

}