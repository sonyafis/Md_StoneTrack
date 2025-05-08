package com.fisun.md_stonetrack.presentation.client.order_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fisun.md_stonetrack.domain.model.Order
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.fisun.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.fisun.md_stonetrack.domain.usecase.LogoutUseCase
import com.fisun.md_stonetrack.domain.usecase.SessionExpiredException
import com.fisun.md_stonetrack.presentation.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class OrderViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val notificationHelper: NotificationHelper,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrderState>(OrderState.Loading)
    val state: StateFlow<OrderState> = _state
    var userName by mutableStateOf("")
    private var isPollingActive by mutableStateOf(true)

    private var lastOrders: List<Order> = emptyList()

    private fun checkForStatusChanges(newOrders: List<Order>) {
        if (lastOrders.isEmpty()) {
            Log.d("StatusCheck", "No previous orders to compare with")
            return
        }

        newOrders.forEach { newOrder ->
            lastOrders.firstOrNull { it.id_order == newOrder.id_order }?.let { oldOrder ->
                if (oldOrder.id_status.status_name != newOrder.id_status.status_name) {
                    Log.i("StatusChange",
                        "Status changed for order ${newOrder.order_number}: " +
                                "${oldOrder.id_status.status_name} -> ${newOrder.id_status.status_name}")

                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            notificationHelper.showStatusChangeNotification(
                                orderNumber = newOrder.order_number,
                                newStatus = newOrder.id_status.status_name // Добавляем новый статус
                            )
                        } catch (e: Exception) {
                            Log.e("NotificationError",
                                "Failed to show notification for order ${newOrder.order_number}", e)
                        }
                    }
                }
            } ?: run {
                Log.d("NewOrder", "New order detected: ${newOrder.order_number}")
            }
        }
    }

    var isRefreshing by mutableStateOf(false)
        private set

    fun refreshOrders() {
        viewModelScope.launch {
            isRefreshing = true
            loadOrders()
            isRefreshing = false
        }
    }

    init {
        loadUserName()
        startPolling()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            userName = user?.first_name ?: user?.name ?: "Пользователь"
        }
    }

    private fun startPolling() = viewModelScope.launch {
        while (isPollingActive) {
            loadOrders()
            delay(15000)
        }
    }

    private suspend fun loadOrders() {
        runCatching {
            getOrdersUseCase().let { newOrders ->
                checkForStatusChanges(newOrders)
                lastOrders = newOrders
                newOrders.filterNot { it.id_status.status_name.equals("Доставлен", true) }
                    .takeIf { it.isNotEmpty() }
                    ?.let { _state.value = OrderState.Success(it) }
                    ?: run { _state.value = OrderState.Empty }
            }
        }.onFailure { e ->
            _state.value = when (e) {
                is SessionExpiredException -> OrderState.SessionExpired.also { stopPolling() }
                is IOException -> OrderState.Error("Проблема с сетью")
                is HttpException -> OrderState.Error("Ошибка сервера: ${e.code()}")
                else -> OrderState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun stopPolling() { isPollingActive = false }
}
