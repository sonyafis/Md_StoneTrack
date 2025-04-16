package com.example.md_stonetrack.data.api

import com.example.md_stonetrack.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.java.KoinJavaComponent.inject

class TokenAuthenticator : Authenticator {
    private val authRepository: AuthRepository by inject(AuthRepository::class.java)

    override fun authenticate(route: Route?, response: Response): Request? {
        // Проверяем, что это 401 ошибка и есть заголовок Authorization
        if (response.code == 401 && response.request.header("Authorization") != null) {
            return runBlocking {
                try {
                    val refreshToken = authRepository.getRefreshToken()
                    if (!refreshToken.isNullOrEmpty()) {
                        val result = authRepository.refreshTokens(refreshToken)
                        if (result.isSuccess) {
                            val newAccessToken = result.getOrNull()?.accessToken
                            if (!newAccessToken.isNullOrEmpty()) {
                                return@runBlocking response.request.newBuilder()
                                    .header("Authorization", "Bearer $newAccessToken")
                                    .build()
                            }
                        }
                    }
                    null // Если не удалось обновить токен
                } catch (e: Exception) {
                    null
                }
            }
        }
        return null
    }
}