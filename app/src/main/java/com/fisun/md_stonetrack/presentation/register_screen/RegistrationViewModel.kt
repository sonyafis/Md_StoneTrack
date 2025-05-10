package com.fisun.md_stonetrack.presentation.register_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.model.RegistrationResponse
import com.fisun.md_stonetrack.domain.usecase.CheckEmailExistsUseCase
import com.fisun.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
import com.fisun.md_stonetrack.domain.usecase.RegisterUseCase
import com.fisun.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import com.fisun.md_stonetrack.domain.usecase.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registerUserUseCase: RegisterUseCase,
    private val checkUsernameExistsUseCase: CheckUsernameExistsUseCase,
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
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

            try {
                val usernameExists = checkUsernameExistsUseCase(username)
                println("Checking email: $email")
                val emailExists = checkEmailExistsUseCase(email)
                println("Email exists: $emailExists")

                when {
                    usernameExists -> {
                        _uiState.value = RegistrationUiState.Error("Логин занят")
                        return@launch
                    }

                    emailExists -> {
                        _uiState.value =
                            RegistrationUiState.Error("Пользователь с таким email уже существует")
                        return@launch
                    }
                }

                val result = registerUserUseCase(
                    username,
                    email,
                    password,
                    first_name,
                    last_name,
                    phone_number
                )

                _uiState.value = if (result.isSuccess) {
                    RegistrationUiState.Success(result.getOrNull()!!)
                } else {
                    RegistrationUiState.Error(
                        result.exceptionOrNull()?.message ?: "Ошибка регистрации"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = RegistrationUiState.Error("Ошибка сети: ${e.message}")
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
