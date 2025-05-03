package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.repository.AuthRepository

class GetAccessTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): String? = authRepository.getAccessToken()
}