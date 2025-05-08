package com.fisun.md_stonetrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(
    @SerialName("id_order") val id_order: Int,
    @SerialName("order_number") val order_number: String,
    @SerialName("address") val address: String,
    @SerialName("description") val description: String?,
    @SerialName("id_status") val id_status: StatusDTO,
    @SerialName("id_client") val id_client: SuperUserDTO,
    @SerialName("id_courier") val id_courier: SuperUserDTO?,
    @SerialName("created_at") val created_at: String,
    @SerialName("delivered_at") val delivered_at: String?
)
