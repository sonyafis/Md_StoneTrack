package com.example.md_stonetrack.presentation.order_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.model.Order
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import com.example.md_stonetrack.domain.usecase.SessionExpiredException
import com.example.md_stonetrack.presentation.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import kotlin.collections.find

class OrderViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val notificationHelper: NotificationHelper,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrderState>(OrderState.Loading)
    val state: StateFlow<OrderState> = _state

    var userName by mutableStateOf("")
        private set

    private var lastOrders: List<Order> = emptyList()
    private var isPollingActive by mutableStateOf(true)

    private fun checkForStatusChanges(newOrders: List<Order>) {
        if (lastOrders.isEmpty()) return

        newOrders.forEach { newOrder ->
            lastOrders.find { it.id_order == newOrder.id_order }?.let { oldOrder ->
                if (oldOrder.id_status.status_name != newOrder.id_status.status_name) {
                    viewModelScope.launch {
                        notificationHelper.showStatusChangeNotification(newOrder.order_number)
                    }
                }
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
        startPollingOrders()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            userName = user?.first_name ?: user?.name ?: "Пользователь"
        }
    }

    private fun startPollingOrders() {
        viewModelScope.launch {
            while (isPollingActive) {
                try {
                    loadOrders()
                } catch (e: Exception) {
                    if (e.message?.contains("Session expired") == true) {
                        _state.value = OrderState.SessionExpired
                        isPollingActive = false
                        break
                    }
                }
                delay(10000L) // каждые 10 секунд
            }
        }
    }

    private suspend fun loadOrders() {
        try {
            val result = getOrdersUseCase()
            if (result.isEmpty()) {
                _state.value = OrderState.Empty
            } else {
                checkForStatusChanges(result)
                // Фильтруем заказы - исключаем доставленные
                val activeOrders = result.filterNot {
                    it.id_status.status_name.equals("Доставлен", ignoreCase = true)
                }
                if (activeOrders.isEmpty()) {
                    _state.value = OrderState.Empty
                } else {
                    _state.value = OrderState.Success(activeOrders)
                }
            }
        } catch (e: SessionExpiredException) {
            _state.value = OrderState.SessionExpired
        } catch (e: IOException) {
            _state.value = OrderState.Error("Проблема с сетью. Проверьте подключение.")
        }
        catch (e: HttpException) {
            _state.value = OrderState.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            _state.value = OrderState.Error(
                when {
                    e.message?.contains("blacklisted") == true -> "Сессия истекла. Требуется повторный вход."
                    else -> e.message ?: "Неизвестная ошибка"
                }
            )
        }
    }

    fun stopPolling() {
        isPollingActive = false
    }
}
