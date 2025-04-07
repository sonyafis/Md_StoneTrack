package com.example.md_stonetrack.data.repository

import com.example.md_stonetrack.data.api.ApiService
import com.example.md_stonetrack.data.model.StatusDTO
import com.example.md_stonetrack.data.model.SuperUserDTO
import com.example.md_stonetrack.domain.model.Order
import com.example.md_stonetrack.domain.model.Status
import com.example.md_stonetrack.domain.model.SuperUser
import com.example.md_stonetrack.domain.repository.OrderRepository

class OrderRepositoryImpl(private val api: ApiService) : OrderRepository {
    override suspend fun getOrders(token: String): List<Order> {
        return try {
            api.getOrders("Bearer $token").mapNotNull { orderDTO ->
                try {
                    Order(
                        id_order = orderDTO.id_order,
                        order_number = orderDTO.order_number,
                        address = orderDTO.address ?: "",
                        description = orderDTO.description,
                        id_status = Status(
                            id_status = orderDTO.id_status.id_status,
                            status_name = orderDTO.id_status.status_name ?: "Unknown"
                        ),
                        id_client = SuperUser(
                            id_super_user = orderDTO.id_client.id_super_user,
                            username = orderDTO.id_client.username ?: "",
                            firstname = orderDTO.id_client.firstname,
                            lastname = orderDTO.id_client.lastname,
                            phone_number = orderDTO.id_client.phone_number,
                            email = orderDTO.id_client.email,
                            type_user = orderDTO.id_client.type_user
                        ),
                        id_courier = orderDTO.id_courier?.let { courierDTO ->
                            SuperUser(
                                id_super_user = courierDTO.id_super_user,
                                username = courierDTO.username ?: "",
                                firstname = courierDTO.firstname,
                                lastname = courierDTO.lastname,
                                phone_number = courierDTO.phone_number,
                                email = courierDTO.email,
                                type_user = courierDTO.type_user
                            )
                        },
                        created_at = orderDTO.created_at ?: "",
                        delivered_at = orderDTO.delivered_at
                    )
                } catch (e: Exception) {
                    null // Пропускаем проблемные записи
                }
            }
        } catch (e: Exception) {
            emptyList() // Возвращаем пустой список при ошибке сети
        }
    }
}