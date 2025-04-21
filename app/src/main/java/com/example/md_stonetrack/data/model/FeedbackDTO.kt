package com.example.md_stonetrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackDTO(
    @SerialName ("user_fullname") val user_fullname: String,
    @SerialName ("email") val email: String,
    @SerialName ("message") val message: String,
    @SerialName ("phone_number") val phone_number: String?,
    @SerialName ("type_feedback") val type_feedback: String,
    @SerialName ("id_super_user") val id_super_user: Int
)
