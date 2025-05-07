package com.kirishhaa.photonotes.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.toApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase
) : ViewModel() {

    private val _data = MutableStateFlow<HomeData?>(null)
    val data = _data.asStateFlow()

    init {
        viewModelScope.launch {
            getEnteredUserUseCase.execute().collect { localUser ->
                _data.value = if (localUser == null) null else HomeData(
                    localUser.imagePath,
                    localUser.name,
                    localUser.language
                )
            }
        }
    }

    class HomeData(
        val photoPath: String?,
        val username: String,
        val lang: Language
    )


    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras.toApp()
                val useCase = GetEnteredUserUseCase(app.localUsersRepository)
                return HomeViewModel(useCase) as T
            }
        }
    }

}