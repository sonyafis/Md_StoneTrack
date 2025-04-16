package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.data.db.Dao.UserDao
import com.example.md_stonetrack.data.db.entities.UserEntity
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
            val access = authRepository.getAccessToken()
            val refresh = authRepository.getRefreshToken()

            if (!access.isNullOrEmpty()) {
                val isAccessValid = runCatching {
                    authRepository.validateAccessToken(access)
                }.getOrElse { false }

                if (isAccessValid) {
                    return getUserRole()
                }

                if (!refresh.isNullOrEmpty()) {
                    val refreshResult = authRepository.refreshTokens(refresh)
                    if (refreshResult.isSuccess) {
                        return getUserRole()
                    }
                }
            }

            authRepository.logout()
            AuthResult.Unauthorized
        } catch (e: Exception) {
            authRepository.logout()
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