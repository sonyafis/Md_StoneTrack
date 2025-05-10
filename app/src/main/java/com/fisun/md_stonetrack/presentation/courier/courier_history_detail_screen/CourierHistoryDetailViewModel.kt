package com.fisun.md_stonetrack.presentation.courier.courier_history_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourierHistoryDetailViewModel(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _orderState = MutableStateFlow(CourierHistoryDetailState())
    val orderState: StateFlow<CourierHistoryDetailState> = _orderState

    fun loadOrderDetail(id_order: Int) {
        viewModelScope.launch {
            _orderState.value = _orderState.value.copy(isLoading = true)
            try {
                val allOrders = getOrdersUseCase()
                val order = allOrders.find { it.id_order == id_order }
                _orderState.value = CourierHistoryDetailState(order = order)
            } catch (e: Exception) {
                _orderState.value =
                    CourierHistoryDetailState(error = e.message ?: "Неизвестная ошибка")
            }
        }
    }
}
