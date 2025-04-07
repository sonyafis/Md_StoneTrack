package com.example.md_stonetrack.data.model

data class SuperUserDTO(
    val id_super_user: Int,
    val username: String,
    val firstname: String,
    val lastname: String,
    val phone_number: String,
    val email: String,
    val type_user: String
)