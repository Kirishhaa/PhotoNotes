package com.kirishhaa.photonotes.presentation.home.feedbackscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kirishhaa.photonotes.domain.feedback.FeedbackEvent
import com.kirishhaa.photonotes.domain.feedback.FeedbackRepository
import com.kirishhaa.photonotes.domain.feedback.SendFeedbackUseCase
import com.kirishhaa.photonotes.domain.users.GetEnteredUserUseCase
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FeedbackViewModel(
    private val getEnteredUserUseCase: GetEnteredUserUseCase,
    private val sendFeedbackUseCase: SendFeedbackUseCase,
    private val localUserMapper: LocalUserMapper,
): ViewModel() {

    private val _events = Channel<FeedbackEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(FeedbackState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val localUser = getEnteredUserUseCase.execute().first()
            val localUserUI = localUserMapper.map(localUser!!)
            _state.value = FeedbackState(
                loadingState = false,
                user = localUserUI
            )
        }
    }

    fun send(usernameValue: String, questionValue: String, wayToFeedback: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(sendingFeedback = true)
            sendFeedbackUseCase.execute(usernameValue, questionValue, wayToFeedback)
            _events.send(FeedbackEvent.SentMessage)
            _state.value = _state.value.copy(sendingFeedback = false)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val repo = LocalUsersRepository.Mock
                val usecase = GetEnteredUserUseCase(repo)
                val usecase2 = SendFeedbackUseCase(FeedbackRepository.Mockk)
                val mapper = LocalUserMapper()
                return FeedbackViewModel(usecase, usecase2, mapper) as T
            }
        }
    }

}