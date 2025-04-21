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

    private val _state = mutableStateOf(FeedbackState())
    private var didRetry = false
    var state by mutableStateOf(FeedbackState())
        private set

    fun onEvent(event: FeedbackEvent) {
        when (event) {
            is FeedbackEvent.UpdateFullname -> state = state.copy(fullname = event.value)
            is FeedbackEvent.UpdateEmail -> state = state.copy(email = event.value)
            is FeedbackEvent.UpdatePhone -> state = state.copy(phone = event.value)
            is FeedbackEvent.UpdateMessage -> state = state.copy(message = event.value)
            is FeedbackEvent.UpdateType -> state = state.copy(type = event.value)
            is FeedbackEvent.Submit -> submitFeedback()
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
            try {
                // Всегда получаем свежий токен
                val currentToken = authRepository.getAccessToken()
                    ?: throw Exception("Not authenticated")
                Log.d("TokenCheck", "Access token: ${currentToken?.take(15)}...")
                sendFeedbackUseCase(
                    token = currentToken,
                    feedback = Feedback(user_fullname = state.fullname,
                        email = state.email,
                        message = state.message,
                        phone_number = state.phone,
                        type_feedback = state.type,
                        id_super_user = state.id_super_user)
                )
                state = state.copy(success = true)
            } catch (e: SessionExpiredException) {
                if (!didRetry) {
                    didRetry = true
                    handleTokenRefresh()
                } else {
                    state = state.copy(error = "Сессия истекла, повторная попытка не удалась.")
                }
            } catch (e: IOException) {
                state = state.copy(error = "Проблема с сетью. Проверьте подключение.")
            }
            catch (e: HttpException) {
                state = state.copy(error = "Ошибка сервера: ${e.code()}")
            } catch (e: Exception) {
                state = state.copy(error = when {
                    e.message?.contains("blacklisted") == true -> {
                        "Сессия истекла. Требуется повторный вход."
                    }
                    else -> e.message ?: "Ошибка отправки"
                })
            } finally {
                didRetry = false
                state = state.copy(isLoading = false)
            }

        }
    }

    private suspend fun handleTokenRefresh() {
        try {
            val refreshToken = authRepository.getRefreshToken()
            Log.d("TokenCheck", "Refresh token: ${refreshToken?.take(15)}...")
            if (refreshToken.isNullOrEmpty()) {
                state = state.copy(error = "Сессия истекла. Требуется повторный вход.")
                return
            }

            val result = authRepository.refreshTokens(refreshToken)
            result.fold(
                onSuccess = {
                    submitFeedback() // Повторяем с новым токеном
                },
                onFailure = {
                    state = state.copy(error = "Сессия истекла. Требуется повторный вход.")
                }
            )
        } catch (e: Exception) {
            state = state.copy(error = "Ошибка обновления сессии: ${e.localizedMessage}")
        }
    }
}