package com.example.md_stonetrack.domain.model

data class Feedback(
    val user_fullname: String,
    val email: String,
    val message: String,
    val phone_number: String?,
    val type_feedback: String,
    val id_super_user: Int
)
