package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.repository.RegistrationRepository

class CheckUsernameExistsUseCase(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke(username: String): Boolean {
        return registrationRepository.checkUsernameExists(username)
    }
}
