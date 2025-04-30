package com.example.md_stonetrack.presentation.client.order_screen

import com.example.md_stonetrack.domain.model.Order

sealed class OrderState {
    object Loading : OrderState()
    object Empty : OrderState()
    data class Success(val orders: List<Order>) : OrderState()
    data class Error(val message: String) : OrderState()
    object SessionExpired : OrderState()
}