package com.example.md_stonetrack.presentation.courier.courier_history_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.model.Order
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CourierHistoryState {
    object Loading : CourierHistoryState()
    object Empty : CourierHistoryState()
    data class Success(val orders: List<Order>) : CourierHistoryState()
    data class Error(val message: String) : CourierHistoryState()
}

class CourierHistoryViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CourierHistoryState>(CourierHistoryState.Loading)
    val state: StateFlow<CourierHistoryState> = _state

    var userName by mutableStateOf("")
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadUserName()
        loadHistory()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            loadHistory()
            isRefreshing = false
        }
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            userName = user?.first_name ?: user?.name ?: "Пользователь"
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.value = CourierHistoryState.Loading
            try {
                val allOrders = getOrdersUseCase()
                val deliveredOrders = allOrders.filter {
                    it.id_status.status_name.equals("Доставлен", ignoreCase = true)
                }.sortedByDescending { it.created_at }

                _state.value = if (deliveredOrders.isEmpty()) {
                    CourierHistoryState.Empty
                } else {
                    CourierHistoryState.Success(deliveredOrders)
                }
            } catch (e: Exception) {
                _state.value = CourierHistoryState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}