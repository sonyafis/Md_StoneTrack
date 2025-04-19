package com.example.md_stonetrack.presentation.profile_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(private val logoutUseCase: LogoutUseCase) : ViewModel() {
    // Состояние для ошибок
    private val _error = mutableStateOf<String?>(null)

    // Метод для выхода из аккаунта
    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase.execute()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}