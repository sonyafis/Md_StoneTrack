package com.example.md_stonetrack.presentation.feedback_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.model.Feedback
import com.example.md_stonetrack.domain.repository.AuthRepository
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.SendFeedbackUseCase
import com.example.md_stonetrack.domain.usecase.SessionExpiredException
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class FeedbackViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val sendFeedbackUseCase: SendFeedbackUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(FeedbackState())
        private set

    fun onEvent(event: FeedbackEvent) = when (event) {
        is FeedbackEvent.Submit -> submitFeedback()
        else -> state = when (event) {
            is FeedbackEvent.UpdateFullname -> state.copy(fullname = event.value)
            is FeedbackEvent.UpdateEmail -> state.copy(email = event.value)
            is FeedbackEvent.UpdatePhone -> state.copy(phone = event.value)
            is FeedbackEvent.UpdateMessage -> state.copy(message = event.value)
            is FeedbackEvent.UpdateType -> state.copy(type = event.value)
            else -> state
        }
    }

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            getCurrentUserUseCase()?.let { user ->
                state = state.copy(
                    fullname = "${user.first_name.orEmpty()} ${user.last_name.orEmpty()}",
                    email = user.email,
                    phone = user.phone_number.orEmpty(),
                    id_super_user = user.id,
                    type = user.type_user.orEmpty() // если хочешь его потом использовать
                )
            }
        }
    }



    private fun submitFeedback() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            runCatching {
                authRepository.getAccessToken()?.let { token ->
                    sendFeedbackUseCase(token, Feedback(
                        user_fullname = state.fullname,
                        email = state.email,
                        message = state.message,
                        phone_number = state.phone,
                        type_feedback = state.type,
                        id_super_user = state.id_super_user
                    ))
                } ?: throw Exception("Not authenticated")
            }.onSuccess {
                state = state.copy(success = true)
            }.onFailure { e ->
                state = state.copy(error = when (e) {
                    is SessionExpiredException -> "Сессия истекла"
                    is IOException -> "Проблема с сетью"
                    is HttpException -> "Ошибка сервера: ${e.code()}"
                    else -> e.message ?: "Ошибка отправки"
                })
            }
            state = state.copy(isLoading = false)
        }
    }
}