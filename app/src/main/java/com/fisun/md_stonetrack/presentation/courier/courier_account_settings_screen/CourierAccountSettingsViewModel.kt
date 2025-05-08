package com.fisun.md_stonetrack.presentation.courier.courier_account_settings_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourierAccountSettingsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AccountSettingsState>(AccountSettingsState.Loading)
    val state: StateFlow<AccountSettingsState> = _state

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase.invoke()
                if (user != null) {
                    _state.value = AccountSettingsState.Success(
                        UserData(
                            name = user.first_name ?: user.name ?: "Пользователь",
                            fullName = "${user.first_name ?: ""} ${user.last_name ?: ""}".trim(),
                            phone = user.phone_number ?: "Не указан",
                            email = user.email ?: "Не указан",
                            login = user.name ?: "Не указан"
                        )
                    )
                } else {
                    _state.value = AccountSettingsState.Error("Пользователь не найден")
                }
            } catch (e: Exception) {
                _state.value = AccountSettingsState.Error("Ошибка загрузки данных: ${e.message}")
            }
        }
    }

    fun updateUserData(newData: UserData) {
        viewModelScope.launch {
            // Здесь будет логика обновления данных через соответствующий use case
            _state.value = AccountSettingsState.Success(newData)
        }
    }

    sealed class AccountSettingsState {
        object Loading : AccountSettingsState()
        data class Success(val userData: UserData) : AccountSettingsState()
        data class Error(val message: String) : AccountSettingsState()
    }
}

data class UserData(
    val name: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val login: String

)