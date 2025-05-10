package com.fisun.md_stonetrack.presentation.client.history_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.model.Order
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.fisun.md_stonetrack.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HistoryState {
    object Loading : HistoryState()
    object Empty : HistoryState()
    data class Success(val orders: List<Order>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

class HistoryViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val state: StateFlow<HistoryState> = _state

    var userName by mutableStateOf("")
        private set

    init {
        loadUserName()
        loadHistory()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            userName = user?.first_name ?: user?.name ?: "Пользователь"
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.value = HistoryState.Loading
            try {
                val allOrders = getOrdersUseCase()
                val deliveredOrders = allOrders.filter {
                    it.id_status.status_name.equals("Доставлен", ignoreCase = true)
                }.sortedByDescending { it.created_at }

                _state.value = if (deliveredOrders.isEmpty()) {
                    HistoryState.Empty
                } else {
                    HistoryState.Success(deliveredOrders)
                }
            } catch (e: Exception) {
                _state.value = HistoryState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}