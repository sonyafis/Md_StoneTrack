package com.example.md_stonetrack.presentation.client.change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.usecase.ChangePasswordUseCase
import com.example.md_stonetrack.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    var current_password by mutableStateOf("")
    var new_password by mutableStateOf("")
    var re_new_password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)

    private val _state = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val state: StateFlow<ChangePasswordState> = _state

    fun onChangePassword() {
        viewModelScope.launch {
            if (new_password != re_new_password) {
                message = "Пароли не совпадают"
                return@launch
            }

            isLoading = true
            val token = getAccessTokenUseCase() ?: run {
                message = "Ошибка авторизации"
                isLoading = false
                return@launch
            }

            val result = changePasswordUseCase(token, current_password, new_password, re_new_password)
            isLoading = false
            message = result.fold(
                onSuccess = { "Пароль успешно изменён" },
                onFailure = { it.message ?: "Неизвестная ошибка" }
            )
        }
    }
}
