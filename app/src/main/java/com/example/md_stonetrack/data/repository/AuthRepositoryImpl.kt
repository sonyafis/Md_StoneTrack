package com.example.md_stonetrack.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.md_stonetrack.data.api.ApiService
import com.example.md_stonetrack.data.db.Dao.UserDao
import com.example.md_stonetrack.data.db.entities.UserEntity
import com.example.md_stonetrack.data.di.dataStore
import com.example.md_stonetrack.data.model.AuthRequest
import com.example.md_stonetrack.domain.model.AuthResult
import com.example.md_stonetrack.domain.model.AuthTokens
import com.example.md_stonetrack.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringPreferencesKey

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context,
    private val userDao: UserDao
) : AuthRepository {

    private val dataStore = context.dataStore

    override suspend fun login(username: String, password: String): AuthResult {
        return try {
            val response = apiService.login(AuthRequest(username, password))

            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    if (authResponse.access != null && authResponse.refresh != null) {
                        // Сохраняем в Room
                        userDao.upsertUser(
                            UserEntity(
                                access = authResponse.access,
                                refresh = authResponse.refresh
                            )
                        )
                        AuthResult.Success(AuthTokens(authResponse.access, authResponse.refresh))
                    } else {
                        AuthResult.Error("Tokens are missing in response")
                    }
                } ?: AuthResult.Error("Empty response body")
            } else {
                AuthResult.Error(response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        }
    }


    override suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            val response = apiService.register(AuthRequest(username, password))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Registration failed: $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        dataStore.edit { it.clear() }
        userDao.clearUser()
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return getAccessToken() != null
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { it[ACCESS_TOKEN_KEY] }.first()
    }

    override suspend fun refreshTokens(refreshToken: String): Result<AuthTokens> {
        return try {
            val response = apiService.refreshToken(com.example.md_stonetrack.data.model.RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse?.access != null && authResponse.refresh != null) {
                    saveTokens(authResponse.access, authResponse.refresh)
                    Result.success(AuthTokens(authResponse.access, authResponse.refresh))
                } else {
                    Result.failure(Exception("Token refresh failed"))
                }
            } else {
                Result.failure(Exception("Refresh failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}
