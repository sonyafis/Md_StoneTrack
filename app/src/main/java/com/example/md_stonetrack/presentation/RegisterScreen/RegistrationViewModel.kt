package com.example.md_stonetrack.presentation.RegisterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.model.RegistrationResponse
import com.example.md_stonetrack.domain.repository.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: StateFlow<RegistrationUiState> = _uiState

    sealed class RegistrationUiState {
        object Idle : RegistrationUiState()
        object Loading : RegistrationUiState()
        data class Error(val message: String) : RegistrationUiState()
        data class Success(val userData: RegistrationResponse) : RegistrationUiState()
    }

    fun setError(message: String) {
        _uiState.value = RegistrationUiState.Error(message)
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        first_name: String?,
        last_name: String?,
        phone_number: String?
    ) {
        viewModelScope.launch {
            println("DEBUG: Register button clicked") // Логирование
            _uiState.value = RegistrationUiState.Loading

            println("DEBUG: Checking username exists") // Логирование
            if (registrationRepository.checkUsernameExists(username)) {
                println("DEBUG: Username already exists") // Логирование
                _uiState.value = RegistrationUiState.Error("Username already exists")
                return@launch
            }
        }

        viewModelScope.launch {
            _uiState.value = RegistrationUiState.Loading

            // Проверка существования username
            if (registrationRepository.checkUsernameExists(username)) {
                _uiState.value = RegistrationUiState.Error("Username already exists")
                return@launch
            }

            // Проверка существования email
            if (registrationRepository.checkEmailExists(email)) {
                _uiState.value = RegistrationUiState.Error("Email already exists")
                return@launch
            }

            val result = registrationRepository.registerUser(
                username,
                email,
                password,
                first_name,
                last_name,
                phone_number
            )

            _uiState.value = when {
                result.isSuccess -> RegistrationUiState.Success(result.getOrNull()!!)
                else -> RegistrationUiState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }
}