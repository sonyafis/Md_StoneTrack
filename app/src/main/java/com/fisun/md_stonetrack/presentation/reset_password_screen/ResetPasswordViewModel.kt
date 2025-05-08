package com.fisun.md_stonetrack.presentation.reset_password_screen
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.data.repository.UserNotFoundException
import com.fisun.md_stonetrack.domain.usecase.ResetPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: androidx.compose.runtime.State<String> = _email

    private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val _emailError = mutableStateOf<String?>(null)
    val emailError: androidx.compose.runtime.State<String?> = _emailError

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onResetPassword() {
        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState.Loading
            _emailError.value = null

            try {
                if (!isEmailValid(_email.value)) {
                    _emailError.value = "Введите правильный адрес электронной почты"
                    _uiState.value = ResetPasswordUiState.Idle
                    return@launch
                }

                resetPasswordUseCase(_email.value)
                _uiState.value = ResetPasswordUiState.Success

            } catch (e: UserNotFoundException) {
                _emailError.value = e.message
            } catch (e: IOException) {
                _uiState.value = ResetPasswordUiState.Error(
                    e.message ?: "Ошибка соединения с сервером"
                )
            } catch (e: Exception) {
                _uiState.value = ResetPasswordUiState.Error(
                    e.message ?: "Неизвестная ошибка"
                )
            } finally {
                if (_uiState.value !is ResetPasswordUiState.Success) {
                    _uiState.value = ResetPasswordUiState.Idle
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    sealed class ResetPasswordUiState {
        object Idle : ResetPasswordUiState()
        object Loading : ResetPasswordUiState()
        object Success : ResetPasswordUiState()
        class Error(val message: String) : ResetPasswordUiState()
    }
}