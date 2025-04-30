package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.repository.OrderRepository

class UpdateOrderStatusUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int, newStatusId: Int): Boolean {
        return repository.updateOrderStatus(orderId, newStatusId)
    }
}
