package com.kirishhaa.photonotes.presentation.languagescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.language.GetLanguagesUseCase
import com.kirishhaa.photonotes.domain.language.LanguageRepository
import com.kirishhaa.photonotes.domain.language.SelectLanguageUseCase
import com.kirishhaa.photonotes.presentation.languagescreen.exceptions.LanguagesAreNotExistException
import com.kirishhaa.photonotes.presentation.languagescreen.exceptions.SelectedLanguageNotFoundException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val userId: Int,
    private val getLanguagesUseCase: GetLanguagesUseCase,
    private val selectLanguageUseCase: SelectLanguageUseCase,
    private val mapper: LanguageStateMapper
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
    val state = _state
        .map { innerState -> mapper.map(innerState) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LanguageStateUI())

    init {
        viewModelScope.launch {
            getLanguagesUseCase.execute().collect { allLanguages ->
                _state.value = _state.value.copy(loading = false, languages = allLanguages)
            }
        }
    }

    fun onNextLanguage() = changeStateOperation {
        val newIndex = getLanguagesIndexByDiff(1)
        val newLanguageId = getLanguageIdByIndex(newIndex)
        selectLanguageUseCase.execute(userId, newLanguageId)
    }

    fun onPreviousLanguage() = changeStateOperation {
        val newIndex = getLanguagesIndexByDiff(-1)
        val newLanguageId = getLanguageIdByIndex(newIndex)
        selectLanguageUseCase.execute(userId, newLanguageId)
    }

    private fun getLanguageIdByIndex(index: Int): String {
        val state = _state.value
        val languages = state.languages ?: throw LanguagesAreNotExistException()
        return languages[index].id
    }

    private fun getLanguagesIndexByDiff(dIndex: Int): Int {
        val state = _state.value
        val languages = state.languages ?: throw LanguagesAreNotExistException()
        val index = languages.indexOfFirst { it.selected }
        if(index == -1) throw SelectedLanguageNotFoundException()
        var newIndex = index + dIndex
        if(newIndex == languages.size) newIndex = 0
        else if(newIndex < 0) newIndex = languages.size - 1
        return newIndex
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
                val repo = LanguageRepository.MockImpl
                return LanguageViewModel(
                    userId = 1,
                    getLanguagesUseCase = GetLanguagesUseCase(repo),
                    selectLanguageUseCase = SelectLanguageUseCase(repo),
                    mapper = LanguageStateMapper()
                ) as T
            }
        }
    }

}