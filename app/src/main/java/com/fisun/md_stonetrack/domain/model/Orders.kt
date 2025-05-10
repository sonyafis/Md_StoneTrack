package com.fisun.md_stonetrack.domain.model

data class Order(
    val id_order: Int,
    val order_number: String,
    val address: String,
    val description: String?,
    val id_status: Status,
    val id_client: SuperUser,
    val id_courier: SuperUser?,
    val created_at: String,
    val delivered_at: String?
)

