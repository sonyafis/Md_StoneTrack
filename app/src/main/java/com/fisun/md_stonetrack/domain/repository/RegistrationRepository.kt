package com.fisun.md_stonetrack.domain.repository

import com.fisun.md_stonetrack.domain.model.RegistrationResponse

interface RegistrationRepository {
    suspend fun registerUser(
        username: String,
        email: String,
        password: String,
        first_name: String?,
        last_name: String?,
        phone_number: String?
    ): Result<RegistrationResponse>

    suspend fun checkUsernameExists(username: String): Boolean
    suspend fun checkEmailExists(email: String): Boolean
}