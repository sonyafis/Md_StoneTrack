package com.fisun.md_stonetrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusDTO(
    @SerialName("id_status") val id_status: Int,
    @SerialName("status_name") val status_name: String
)