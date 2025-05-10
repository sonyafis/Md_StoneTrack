package com.fisun.md_stonetrack.presentation.client.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.fisun.md_stonetrack.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    private val _navigationEvent = MutableStateFlow<ProfileEvent?>(null)
    val navigationEvent: StateFlow<ProfileEvent?> = _navigationEvent

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase.invoke()
                _state.value =
                    ProfileState.Success(user?.first_name ?: user?.name ?: "Пользователь")
            } catch (e: Exception) {
                _state.value = ProfileState.Error("Ошибка загрузки данных")
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        _navigationEvent.value = event
    }

    fun resetNavigationEvent() {
        _navigationEvent.value = null
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute()
            _navigationEvent.value = ProfileEvent.NavigateToLogin
        }
    }

    sealed class ProfileEvent {
        object NavigateToSettings : ProfileEvent()
        object NavigateToLogin : ProfileEvent()
        object Logout : ProfileEvent()
    }

    sealed class ProfileState {
        object Loading : ProfileState()
        data class Success(val userName: String) : ProfileState()
        data class Error(val message: String) : ProfileState()
    }
}