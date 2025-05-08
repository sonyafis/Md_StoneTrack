package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.repository.AuthRepository

class GetCurrentUserByIdUseCase (private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Int? {
        return authRepository.getCurrentUserId()
    }
}