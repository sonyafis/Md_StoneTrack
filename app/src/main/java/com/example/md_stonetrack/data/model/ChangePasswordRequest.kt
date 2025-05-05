package com.example.md_stonetrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @SerialName("current_password") val current_password: String,
    @SerialName("new_password") val new_password: String,
    @SerialName("re_new_password") val re_new_password: String
)
