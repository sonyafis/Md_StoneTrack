package com.fisun.md_stonetrack.presentation.start_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    sealed class NavigationEvent {
        object NavigateToLogin : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateToLogin)
        }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateToRegister)
        }
    }
}