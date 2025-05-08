package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.data.db.entities.UserEntity
import com.fisun.md_stonetrack.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): UserEntity? = repository.getCurrentUser()
}
