package com.fisun.md_stonetrack.presentation.courier.courier_profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.usecase.DeleteAccountUseCase
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.fisun.md_stonetrack.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourierProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
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
                _state.value = ProfileState.Success(user?.first_name ?: user?.name ?: "Пользователь")
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
        }
    }

    private val _deleteAccountState = MutableStateFlow<DeleteAccountState>(DeleteAccountState.Idle)
    val deleteAccountState: StateFlow<DeleteAccountState> = _deleteAccountState

    fun deleteAccount() {
        viewModelScope.launch {
            _deleteAccountState.value = DeleteAccountState.Loading
            when (val result = deleteAccountUseCase()) {
                DeleteAccountUseCase.Result.Success -> {
                    _deleteAccountState.value = DeleteAccountState.Success
                    _navigationEvent.value = ProfileEvent.DeleteAccount
                }
                is DeleteAccountUseCase.Result.Error -> {
                    _deleteAccountState.value = DeleteAccountState.Error(result.message)
                }
            }
        }
    }

    sealed class DeleteAccountState {
        object Idle : DeleteAccountState()
        object Loading : DeleteAccountState()
        object Success : DeleteAccountState()
        data class Error(val message: String) : DeleteAccountState()
    }

    sealed class ProfileEvent {
        object NavigateToAbout : ProfileEvent()
        object NavigateToSettings : ProfileEvent()
        object Logout : ProfileEvent()
        object DeleteAccount : ProfileEvent()
    }

    sealed class ProfileState {
        object Loading : ProfileState()
        data class Success(val userName: String) : ProfileState()
        data class Error(val message: String) : ProfileState()
    }
}