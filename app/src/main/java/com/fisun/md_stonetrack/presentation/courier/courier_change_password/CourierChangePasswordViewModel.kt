package com.fisun.md_stonetrack.presentation.courier.courier_change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.usecase.ChangePasswordUseCase
import com.fisun.md_stonetrack.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourierChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    var current_password by mutableStateOf("")
    var new_password by mutableStateOf("")
    var re_new_password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)
    var passwordErrors by mutableStateOf<Map<String, String?>>(emptyMap())

    private val _state =
        MutableStateFlow<CourierChangePasswordState>(CourierChangePasswordState.Idle)
    val state: StateFlow<CourierChangePasswordState> = _state

    private fun validatePasswords(): Boolean {
        val errors = mutableMapOf<String, String?>()
        var isValid = true

        if (current_password.isBlank()) {
            errors["current_password"] = "Введите текущий пароль"
            isValid = false
        }

        if (new_password.isBlank()) {
            errors["new_password"] = "Введите новый пароль"
            isValid = false
        } else {
            if (new_password.length < 8) {
                errors["new_password"] = "Пароль должен содержать минимум 8 символов"
                isValid = false
            } else if (!new_password.any { it.isDigit() }) {
                errors["new_password"] = "Пароль должен содержать хотя бы одну цифру"
                isValid = false
            } else if (!new_password.any { it.isUpperCase() }) {
                errors["new_password"] = "Пароль должен содержать хотя бы одну заглавную букву"
                isValid = false
            } else if (!new_password.any { it.isLowerCase() }) {
                errors["new_password"] = "Пароль должен содержать хотя бы одну строчную букву"
                isValid = false
            } else if (new_password.any { it.isWhitespace() }) {
                errors["new_password"] = "Пароль не должен содержать пробелов"
                isValid = false
            }
        }

        if (re_new_password.isBlank()) {
            errors["re_new_password"] = "Подтвердите новый пароль"
            isValid = false
        } else if (re_new_password != new_password) {
            errors["re_new_password"] = "Пароли не совпадают"
            isValid = false
        }

        passwordErrors = errors
        return isValid
    }

    fun onChangePassword() {
        viewModelScope.launch {
            if (!validatePasswords()) {
                message = "Исправьте ошибки в форме"
                return@launch
            }

            isLoading = true
            val token = getAccessTokenUseCase() ?: run {
                message = "Ошибка авторизации"
                isLoading = false
                return@launch
            }

            val result =
                changePasswordUseCase(token, current_password, new_password, re_new_password)
            isLoading = false
            message = result.fold(
                onSuccess = { "Пароль успешно изменён" },
                onFailure = { it.message ?: "Неизвестная ошибка" }
            )
        }
    }
}