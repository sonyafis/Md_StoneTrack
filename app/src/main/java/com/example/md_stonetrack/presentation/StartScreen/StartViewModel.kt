package com.example.md_stonetrack.presentation.StartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

// presentation/viewmodel/StartViewModel.kt
class StartViewModel : ViewModel() {
    // Создаем поток для событий навигации
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    // События навигации
    sealed class NavigationEvent {
        object NavigateToLogin : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
    }

    // Обработка нажатия кнопки входа
    fun onLoginClick() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateToLogin)
        }
    }

    // Обработка нажатия кнопки регистрации
    fun onRegisterClick() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateToRegister)
        }
    }
}