package com.example.md_stonetrack.presentation.client.feedback_screen

data class FeedbackState(
    val fullname: String = "",
    val email: String = "",
    val phone: String = "",
    val message: String = "",
    val type: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val token: String = "",
    val id_super_user: Int = 1
)