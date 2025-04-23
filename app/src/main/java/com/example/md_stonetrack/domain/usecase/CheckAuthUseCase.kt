package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.data.db.Dao.UserDao
import com.example.md_stonetrack.domain.repository.AuthRepository

class CheckAuthUseCase(
    private val authRepository: AuthRepository,
    private val userDao: UserDao
) {
    sealed class AuthResult {
        data class Authorized(val userRole: String) : AuthResult()
        object Unauthorized : AuthResult()
    }

    suspend operator fun invoke(): AuthResult {
        return try {
            val accessToken = authRepository.getAccessToken()

            when {
                accessToken == null -> AuthResult.Unauthorized
                authRepository.validateAccessToken(accessToken) -> {
                    authRepository.getCurrentUser()?.let { user ->
                        AuthResult.Authorized(user.type_user ?: "default")
                    } ?: AuthResult.Unauthorized
                }
                else -> {
                    val refreshToken = authRepository.getRefreshToken()
                    if (!refreshToken.isNullOrEmpty()) {
                        authRepository.refreshTokens(refreshToken).fold(
                            onSuccess = {
                                authRepository.getCurrentUser()?.let { user ->
                                    AuthResult.Authorized(user.type_user ?: "default")
                                } ?: AuthResult.Unauthorized
                            },
                            onFailure = { AuthResult.Unauthorized }
                        )
                    } else AuthResult.Unauthorized
                }
            }
        } catch (e: Exception) {
            AuthResult.Unauthorized
        }
    }

    private suspend fun getUserRole(): AuthResult {
        val user = userDao.getUser()
        return if (user?.type_user != null) {
            AuthResult.Authorized(user.type_user!!)
        } else {
            AuthResult.Unauthorized
        }
    }
}