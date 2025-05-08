package com.fisun.md_stonetrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuperUserDTO(
    @SerialName("id_super_user") val id_super_user: Int,
    @SerialName("username") val username: String,
    @SerialName("first_name") val first_name: String? = null,
    @SerialName("last_name") val last_name: String? = null,
    @SerialName("phone_number") val phone_number: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("type_user") val type_user: String
)