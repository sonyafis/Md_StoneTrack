package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.model.Order
import com.fisun.md_stonetrack.domain.repository.AuthRepository
import com.fisun.md_stonetrack.domain.repository.OrderRepository
import retrofit2.HttpException
import java.io.IOException

class GetOrdersUseCase(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): List<Order> {
        return try {
            val accessToken = authRepository.getAccessToken() ?: throw SessionExpiredException()
            val refreshToken = authRepository.getRefreshToken() ?: throw SessionExpiredException()

            try {
                orderRepository.getOrders(accessToken)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    handleUnauthorizedError(refreshToken)
                } else {
                    throw IOException("Ошибка сети: ${e.message}")
                }
            }
        } catch (e: Exception) {
            throw when (e) {
                is SessionExpiredException -> e
                else -> IOException("Не удалось получить заказы: ${e.message}")
            }
        }
    }

    private suspend fun handleUnauthorizedError(refreshToken: String): List<Order> {
        return try {
            val refreshResult = authRepository.refreshTokens(refreshToken)
            when {
                refreshResult.isSuccess -> {
                    val newAccessToken = refreshResult.getOrNull()?.accessToken
                        ?: throw SessionExpiredException()
                    orderRepository.getOrders(newAccessToken)
                }

                else -> throw SessionExpiredException()
            }
        } catch (e: Exception) {
            throw SessionExpiredException()
        }
    }
}

class SessionExpiredException : Exception("Сессия истекла, пожалуйста, войдите снова")