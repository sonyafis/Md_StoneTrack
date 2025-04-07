package com.example.md_stonetrack.data.model

data class OrderDTO(
    val id_order: Int,
    val order_number: Int,
    val address: String,
    val description: String?,
    val id_status: StatusDTO,
    val id_client: SuperUserDTO,
    val id_courier: SuperUserDTO?,
    val created_at: String,
    val delivered_at: String?
)
