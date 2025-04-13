package com.example.md_stonetrack.presentation.SplashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.usecase.CheckAuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkAuthUseCase: CheckAuthUseCase
) : ViewModel() {

    sealed class AuthCheckState {
        object Loading : AuthCheckState()
        data class Authorized(val role: String) : AuthCheckState()
        object Unauthorized : AuthCheckState()
    }

    private val _uiState = MutableStateFlow<AuthCheckState>(AuthCheckState.Loading)
    val uiState: StateFlow<AuthCheckState> = _uiState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            when (val result = checkAuthUseCase()) {
                is CheckAuthUseCase.AuthResult.Authorized -> {
                    _uiState.value = AuthCheckState.Authorized(result.userRole)
                }
                is CheckAuthUseCase.AuthResult.Unauthorized -> {
                    _uiState.value = AuthCheckState.Unauthorized
                }
            }
        }
    }
}