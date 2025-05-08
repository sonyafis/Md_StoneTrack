package com.fisun.md_stonetrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("access") val access: String? = null,
    @SerialName("refresh") val refresh: String? = null,
)

