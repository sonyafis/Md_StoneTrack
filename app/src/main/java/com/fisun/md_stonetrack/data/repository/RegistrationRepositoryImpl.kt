package com.fisun.md_stonetrack.data.repository

import com.fisun.md_stonetrack.data.api.ApiService
import com.fisun.md_stonetrack.domain.model.RegistrationRequest
import com.fisun.md_stonetrack.domain.model.RegistrationResponse
import com.fisun.md_stonetrack.domain.repository.RegistrationRepository

class RegistrationRepositoryImpl(
    private val apiService: ApiService
) : RegistrationRepository {
    override suspend fun registerUser(
        username: String,
        email: String,
        password: String,
        first_name: String?,
        last_name: String?,
        phone_number: String?
    ): Result<RegistrationResponse> {
        println("DEBUG: Trying to register user")
        return try {
            val response = apiService.registerUser(
                RegistrationRequest(
                    username = username,
                    email = email,
                    password = password,
                    re_password = password,
                    first_name = first_name,
                    last_name = last_name,
                    phone_number = phone_number
                )
            )

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string() ?: "Регистрация не удалась"
                    )
                )
            }
        } catch (e: Exception) {
            println("DEBUG: Ошибка регистрации: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun checkUsernameExists(username: String): Boolean {
        return try {
            val response = apiService.checkUsernameExists(username)
            if (!response.isSuccessful) {
                return false
            }
            response.body() ?: false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val response = apiService.checkEmailExists(email)
            println("CheckEmailExists Response: ${response.code()}, Body: ${response.body()}")
            response.isSuccessful && response.body() == true
        } catch (e: Exception) {
            println("CheckEmailExists Error: ${e.message}")
            false
        }
    }
}