package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.repository.OrderRepository
import com.example.md_stonetrack.domain.model.Order
import com.example.md_stonetrack.domain.repository.AuthRepository

class GetOrdersUseCase(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): List<Order> {
        val token = authRepository.getAccessToken()
        return if (token != null) {
            orderRepository.getOrders(token)
        } else {
            emptyList()
        }
    }
}


