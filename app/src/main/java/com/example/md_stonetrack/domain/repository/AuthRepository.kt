package com.example.md_stonetrack.domain.repository

import com.example.md_stonetrack.domain.model.AuthResult
import com.example.md_stonetrack.domain.model.AuthTokens

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
    suspend fun logout()
    suspend fun isUserAuthenticated(): Boolean
    suspend fun getAccessToken(): String?
    suspend fun refreshTokens(refreshToken: String): Result<AuthTokens>
}
