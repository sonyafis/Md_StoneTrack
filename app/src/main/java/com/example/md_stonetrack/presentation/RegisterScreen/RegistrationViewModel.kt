package com.example.md_stonetrack.presentation.RegisterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.model.RegistrationResponse
import com.example.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
import com.example.md_stonetrack.domain.usecase.RegisterUseCase
import com.example.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import com.example.md_stonetrack.domain.usecase.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registerUserUseCase: RegisterUseCase,
    private val checkUsernameExistsUseCase: CheckUsernameExistsUseCase,
    private val validateRegistrationFieldsUseCase: ValidateRegistrationFieldsUseCase
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
            _uiState.value = RegistrationUiState.Loading

            // Проверка существования username
            if (checkUsernameExistsUseCase(username)) {
                _uiState.value = RegistrationUiState.Error("Username already exists")
                return@launch
            }

            val result = registerUserUseCase(
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

    fun validateRegistrationFields(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ): ValidationResult {
        return validateRegistrationFieldsUseCase(
            username,
            email,
            password,
            confirmPassword,
            firstName,
            lastName,
            phoneNumber
        )
    }
}
