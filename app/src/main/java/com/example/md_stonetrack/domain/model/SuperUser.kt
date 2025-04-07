package com.example.md_stonetrack.domain.model

data class SuperUser(
    val id_super_user: Int,
    val username: String,
    val firstname: String?,
    val lastname: String?,
    val phone_number: String?,
    val email: String?,
    val type_user: String
)