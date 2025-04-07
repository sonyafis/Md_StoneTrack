package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.repository.OrderRepository
import com.example.md_stonetrack.domain.model.Order

class GetOrdersUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(token: String): List<Order> {
        println("Передаём токен в репозиторий: $token") // Лог
        return repository.getOrders(token)
    }
}

