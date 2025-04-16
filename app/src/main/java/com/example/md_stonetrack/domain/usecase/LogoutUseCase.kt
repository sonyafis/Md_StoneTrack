package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend fun execute() {
        authRepository.logout()
    }
}
