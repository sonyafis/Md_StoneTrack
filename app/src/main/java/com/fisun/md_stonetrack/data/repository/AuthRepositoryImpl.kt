package com.fisun.md_stonetrack.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.fisun.md_stonetrack.data.api.ApiService
import com.fisun.md_stonetrack.data.db.Dao.UserDao
import com.fisun.md_stonetrack.data.db.entities.UserEntity
import com.fisun.md_stonetrack.di.dataStore
import com.fisun.md_stonetrack.data.model.AuthRequest
import com.fisun.md_stonetrack.domain.model.AuthResult
import com.fisun.md_stonetrack.domain.model.AuthTokens
import com.fisun.md_stonetrack.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fisun.md_stonetrack.data.model.ChangePasswordRequest
import com.fisun.md_stonetrack.data.model.RefreshTokenRequest
import com.fisun.md_stonetrack.data.model.ResetPasswordRequest
import com.fisun.md_stonetrack.data.security.CryptoManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthRepositoryImpl(
    private val apiService: ApiService, private val context: Context, private val userDao: UserDao
) : AuthRepository {

    private val dataStore = context.dataStore
    private val cryptoManager = CryptoManager()
    private val tokenValidationMutex = Mutex()
    private var isTokenValidCache: Boolean? = null

    override suspend fun refreshTokens(refreshToken: String): Result<AuthTokens> {
        return try {
            tokenValidationMutex.withLock {
                isTokenValidCache = null
                apiService.refreshToken(RefreshTokenRequest(refreshToken)).let { response ->
                    when {
                        response.isSuccessful -> response.body()?.let { authResponse ->
                            if (authResponse.access != null && authResponse.refresh != null) {
                                saveTokens(authResponse.access, authResponse.refresh)
                                Result.success(
                                    AuthTokens(
                                        authResponse.access, authResponse.refresh
                                    )
                                )
                            } else {
                                Result.failure(Exception("Tokens missing"))
                            }
                        } ?: Result.failure(Exception("Empty response"))

                        response.code() == 401 -> {
                            logout()
                            Result.failure(Exception("Refresh token invalid"))
                        }

                        else -> Result.failure(Exception("Refresh failed"))
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(username: String, password: String): AuthResult {
        return try {
            val response = apiService.login(AuthRequest(username, password))

            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    if (authResponse.access != null && authResponse.refresh != null) {
                        saveTokens(authResponse.access, authResponse.refresh)

                        val userDetailsResponse =
                            apiService.getUserDetails("Bearer ${authResponse.access}")

                        if (userDetailsResponse.isSuccessful) {
                            val userDetails = userDetailsResponse.body()

                            val userId =
                                userDetails?.id_super_user
                                    ?: throw Exception("ID пользователя null")
                            saveUserId(userId)

                            userDao.upsertUser(
                                UserEntity(
                                    id = userId,
                                    name = username,
                                    email = userDetails?.email ?: "",
                                    first_name = userDetails?.first_name,
                                    last_name = userDetails?.last_name,
                                    phone_number = userDetails?.phone_number,
                                    type_user = userDetails?.type_user
                                )
                            )

                            return AuthResult.Success(
                                AuthTokens(
                                    accessToken = authResponse.access,
                                    refreshToken = authResponse.refresh,
                                    userType = userDetails?.type_user
                                )
                            )
                        }
                    } else {
                        AuthResult.Error("Токены отсутствуют")
                    }
                } ?: AuthResult.Error("Пустое поле")
            } else {
                AuthResult.Error(response.errorBody()?.string() ?: "Неизвестная ошибка")
            }
        } catch (e: Exception) {
            AuthResult.Error("Ошибка сети: ${e.message}")
        } as AuthResult
    }

    override suspend fun validateAccessToken(token: String): Boolean {
        return try {
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
        dataStore.edit { preferences ->
            preferences.clear()
        }
        userDao.clearUser()
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

    override suspend fun getCurrentUser(): UserEntity? {
        val userId = getCurrentUserId() ?: return null
        return userDao.getUserById(userId)
    }

    override suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    override suspend fun getCurrentUserId(): Int? {
        return dataStore.data.map { it[USER_ID_KEY] }.firstOrNull()
    }

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val USER_ID_KEY = intPreferencesKey("user_id")
    }

    override suspend fun changePassword(
        token: String, current_password: String, new_password: String, re_new_password: String
    ): Result<Unit> {
        return try {
            val response = apiService.changePassword(
                token = "Bearer $token",
                request = ChangePasswordRequest(current_password, new_password, re_new_password)
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            val response = apiService.resetPassword(ResetPasswordRequest(email))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)

                if (errorMessage.contains("не существует", ignoreCase = true)) {
                    Result.failure(UserNotFoundException(errorMessage))
                } else {
                    Result.failure(Exception("Ошибка сервера: ${response.code()} - $errorMessage"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}"))
        }
    }

    private fun parseErrorMessage(json: String?): String {
        return try {
            val jsonArray = org.json.JSONArray(json)
            jsonArray.getString(0)
        } catch (e: Exception) {
            json ?: "Неизвестная ошибка"
        }
    }

    class UserNotFoundException(message: String) : Exception(message)
}
