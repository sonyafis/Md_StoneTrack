package com.fisun.md_stonetrack.domain.repository

import com.fisun.md_stonetrack.data.db.entities.UserEntity
import com.fisun.md_stonetrack.domain.model.AuthResult
import com.fisun.md_stonetrack.domain.model.AuthTokens

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
    suspend fun logout()
    suspend fun getCurrentUser(): UserEntity?
    suspend fun isUserAuthenticated(): Boolean
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun refreshTokens(refreshToken: String): Result<AuthTokens>
    suspend fun validateAccessToken(token: String): Boolean
    suspend fun saveUserId(userId: Int)
    suspend fun getCurrentUserId(): Int?
    suspend fun changePassword(
        token: String,
        current_password: String,
        new_password: String,
        re_new_password: String
    ): Result<Unit>

    suspend fun resetPassword(email: String): Result<Unit>
}
