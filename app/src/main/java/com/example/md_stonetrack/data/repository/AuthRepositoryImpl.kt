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
import com.example.md_stonetrack.data.model.RefreshTokenRequest
import com.example.md_stonetrack.data.security.CryptoManager

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context,
    private val userDao: UserDao
) : AuthRepository {

    private val dataStore = context.dataStore
    private val cryptoManager = CryptoManager()

    override suspend fun login(username: String, password: String): AuthResult {
        return try {
            val response = apiService.login(AuthRequest(username, password))

            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    if (authResponse.access != null && authResponse.refresh != null) {
                        // Сохраняем токены в DataStore
                        saveTokens(authResponse.access, authResponse.refresh)

                        // Получаем детали пользователя
                        val userDetailsResponse = apiService.getUserDetails("Bearer ${authResponse.access}")

                        if (userDetailsResponse.isSuccessful) {
                            val userDetails = userDetailsResponse.body()

                            // Сохраняем данные пользователя в Room
                            userDao.upsertUser(
                                UserEntity(
                                    id = 1,
                                    name = username,
                                    email = userDetails?.email ?: "",
                                    first_name = userDetails?.first_name,
                                    last_name = userDetails?.last_name,
                                    phone_number = userDetails?.phone_number,
                                    type_user = userDetails?.type_user
                                )
                            )

                            // Возвращаем токены + роль пользователя
                            return AuthResult.Success(
                                AuthTokens(
                                    accessToken = authResponse.access,
                                    refreshToken = authResponse.refresh,
                                    userType = userDetails?.type_user // <- внутри успешного ответа
                                )
                            )
                        }
                    } else {
                        AuthResult.Error("Tokens are missing in response")
                    }
                } ?: AuthResult.Error("Empty response body")
            } else {
                AuthResult.Error(response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        } as AuthResult
    }

    override suspend fun validateAccessToken(token: String): Boolean {
        return try {
            // Используем любой защищенный endpoint для проверки
            val response = apiService.getUserDetails("Bearer $token")
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        val encryptedAccess = cryptoManager.encrypt(accessToken)
        val encryptedRefresh = cryptoManager.encrypt(refreshToken)
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encryptedAccess
            preferences[REFRESH_TOKEN_KEY] = encryptedRefresh
        }
    }

    override suspend fun logout() {
        dataStore.edit { it.clear() } // Очистить токены в DataStore
        userDao.clearUser() // Очистить данные пользователя в Room
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return getAccessToken() != null
    }

    override suspend fun getAccessToken(): String? {
        val encryptedToken = dataStore.data.map { it[ACCESS_TOKEN_KEY] }.first()
        return encryptedToken?.let { cryptoManager.decrypt(it) }
    }

    override suspend fun getRefreshToken(): String? {
        val encryptedToken = dataStore.data.map { it[REFRESH_TOKEN_KEY] }.first()
        return encryptedToken?.let { cryptoManager.decrypt(it) }
    }

    override suspend fun refreshTokens(refreshToken: String): Result<AuthTokens> {
        return try {
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse?.access != null && authResponse.refresh != null) {
                    saveTokens(authResponse.access, authResponse.refresh)

                    // пробуем получить роль из Room
                    val cachedUser = userDao.getUserById(1)

                    Result.success(
                        AuthTokens(
                            authResponse.access,
                            authResponse.refresh,
                            userType = cachedUser?.type_user // здесь если есть
                        )
                    )
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


    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}
