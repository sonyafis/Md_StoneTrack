package com.fisun.md_stonetrack.domain.model

data class RegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    val re_password: String,
    val first_name: String? = null,
    val last_name: String? = null,
    val phone_number: String? = null
)