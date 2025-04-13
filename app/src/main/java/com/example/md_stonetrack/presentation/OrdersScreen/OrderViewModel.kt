package com.example.md_stonetrack.presentation.OrdersScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrderState>(OrderState.Loading)
    val state: StateFlow<OrderState> = _state

    var userName by mutableStateOf("")
        private set

    init {
        loadUserName()
        loadOrders()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            userName = user?.first_name ?: user?.name ?: "Пользователь"
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.value = OrderState.Loading
            val result = getOrdersUseCase()
            _state.value = if (result.isEmpty()) {
                OrderState.Empty
            } else {
                OrderState.Success(result)
            }
        }
    }
}
