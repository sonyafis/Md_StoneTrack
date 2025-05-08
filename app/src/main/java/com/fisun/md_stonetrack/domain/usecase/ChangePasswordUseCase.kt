package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.repository.AuthRepository

class ChangePasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(token: String, current_password: String, new_password: String, re_new_password: String): Result<Unit> {
        return repository.changePassword(token, current_password, new_password, re_new_password)
    }
}
