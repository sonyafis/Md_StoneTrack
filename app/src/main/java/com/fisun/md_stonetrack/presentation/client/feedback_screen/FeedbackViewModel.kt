package com.fisun.md_stonetrack.presentation.client.feedback_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.model.Feedback
import com.fisun.md_stonetrack.domain.usecase.GetAccessTokenUseCase
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserByIdUseCase
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.fisun.md_stonetrack.domain.usecase.SendFeedbackUseCase
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class FeedbackViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val sendFeedbackUseCase: SendFeedbackUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserByIdUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
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
                )
            }
        }
    }


    private fun submitFeedback() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = runCatching {
                val token = getAccessTokenUseCase() ?: throw Exception("Not authenticated")
                val userId = getCurrentUserIdUseCase()
                if (token == null || userId == null) {
                    throw Exception("Не авторизован")
                }
                sendFeedbackUseCase(
                    token, Feedback(
                        user_fullname = state.fullname,
                        email = state.email,
                        message = state.message,
                        phone_number = state.phone,
                        type_feedback = state.type,
                        id_super_user = userId
                    )
                )
            }
            state = when {
                result.isSuccess -> state.copy(success = true)
                else -> state.copy(
                    error = when (val e = result.exceptionOrNull()) {
                        is IOException -> "Проблема с сетью"
                        is HttpException -> "Ошибка сервера: ${e.code()}"
                        else -> e?.message ?: "Ошибка отправки"
                    }
                )
            }.copy(isLoading = false)

        }
    }
}