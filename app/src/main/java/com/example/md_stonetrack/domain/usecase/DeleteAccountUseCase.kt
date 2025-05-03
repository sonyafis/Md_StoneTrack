package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.repository.AuthRepository

class DeleteAccountUseCase(private val authRepository: AuthRepository) {
    sealed class Result {
        object Success : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(): Result {
        return try {
            val success = authRepository.deleteAccount()
            if (success) Result.Success
            else Result.Error("Не удалось удалить аккаунт")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Произошла неизвестная ошибка")
        }
    }
}