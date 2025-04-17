package com.example.md_stonetrack.domain.usecase

import com.example.md_stonetrack.domain.model.RegistrationRequest
import com.example.md_stonetrack.domain.model.RegistrationResponse
import com.example.md_stonetrack.domain.repository.RegistrationRepository

class RegisterUseCase(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        first_name: String?,
        last_name: String?,
        phone_number: String?
    ): Result<RegistrationResponse> {
        return registrationRepository.registerUser(
            username,
            email,
            password,
            first_name,
            last_name,
            phone_number
        )
    }
}
