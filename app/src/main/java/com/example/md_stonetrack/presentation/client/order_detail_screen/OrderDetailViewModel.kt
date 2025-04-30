package com.example.md_stonetrack.presentation.client.order_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val getOrdersUseCase: GetOrdersUseCase // или отдельный use case для одного заказа
) : ViewModel() {

    private val _orderState = MutableStateFlow(OrderDetailState())
    val orderState: StateFlow<OrderDetailState> = _orderState

    fun loadOrderDetail(id_order: Int) {
        viewModelScope.launch {
            _orderState.value = _orderState.value.copy(isLoading = true)
            try {
                val allOrders = getOrdersUseCase()
                val order = allOrders.find { it.id_order == id_order }
                _orderState.value = OrderDetailState(order = order)
            } catch (e: Exception) {
                _orderState.value = OrderDetailState(error = e.message ?: "Неизвестная ошибка")
            }
        }
    }
}
