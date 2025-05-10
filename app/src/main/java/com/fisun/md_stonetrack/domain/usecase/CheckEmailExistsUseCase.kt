package com.fisun.md_stonetrack.domain.usecase

import com.fisun.md_stonetrack.domain.repository.RegistrationRepository

class CheckEmailExistsUseCase(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return registrationRepository.checkEmailExists(email)
    }
}
