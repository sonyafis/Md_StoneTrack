package com.example.md_stonetrack.domain.repository

import com.example.md_stonetrack.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(token: String): List<Order>
    suspend fun updateOrderStatus(orderId: Int, newStatusId: Int): Boolean
}


