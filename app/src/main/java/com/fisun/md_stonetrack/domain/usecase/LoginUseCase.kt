package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.model.AuthResult
import com.fisun.md_stonetrack.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): AuthResult {
        return repository.login(username, password)
    }
}
