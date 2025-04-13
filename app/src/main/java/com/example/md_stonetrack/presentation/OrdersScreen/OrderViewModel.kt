package com.example.md_stonetrack.presentation.OrdersScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.domain.model.Order
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import java.io.IOException
import android.util.Log
import com.example.md_stonetrack.domain.repository.AuthRepository

class OrderViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _orders = mutableStateOf<List<Order>>(emptyList())
    private val _error = mutableStateOf<String?>(null)
    private val _isLoading = mutableStateOf(false)

    val orders: State<List<Order>> = _orders
    val error: State<String?> = _error
    val isLoading: State<Boolean> = _isLoading

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _error.value = null

                val token = authRepository.getAccessToken()
                if (token.isNullOrEmpty()) {
                    _error.value = "Токен не найден"
                    return@launch
                }

                _orders.value = getOrdersUseCase(token)
            } catch (e: Exception) {
                _error.value = when (e) {
                    is IOException -> "Сетевая ошибка: ${e.message}"
                    else -> "Ошибка: ${e.localizedMessage}"
                }
                Log.e("API", "Ошибка загрузки", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
