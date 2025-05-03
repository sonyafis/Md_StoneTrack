package com.example.md_stonetrack.domain.repository

import com.example.md_stonetrack.data.db.entities.UserEntity
import com.example.md_stonetrack.domain.model.AuthResult
import com.example.md_stonetrack.domain.model.AuthTokens

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
    suspend fun logout()
    suspend fun getCurrentUser(): UserEntity?
    suspend fun isUserAuthenticated(): Boolean
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun refreshTokens(refreshToken: String): Result<AuthTokens>
    suspend fun validateAccessToken(token: String): Boolean
    suspend fun deleteAccount(): Boolean
    suspend fun saveUserId(userId: Int)
    suspend fun getCurrentUserId(): Int?
}
