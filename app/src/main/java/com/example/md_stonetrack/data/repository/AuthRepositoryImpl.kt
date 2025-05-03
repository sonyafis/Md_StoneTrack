package com.example.md_stonetrack.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.md_stonetrack.data.api.ApiService
import com.example.md_stonetrack.data.db.Dao.UserDao
import com.example.md_stonetrack.data.db.entities.UserEntity
import com.example.md_stonetrack.di.dataStore
import com.example.md_stonetrack.data.model.AuthRequest
import com.example.md_stonetrack.domain.model.AuthResult
import com.example.md_stonetrack.domain.model.AuthTokens
import com.example.md_stonetrack.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.md_stonetrack.data.model.RefreshTokenRequest
import com.example.md_stonetrack.data.security.CryptoManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context,
    private val userDao: UserDao
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
                                Result.success(AuthTokens(authResponse.access, authResponse.refresh))
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

                        val userDetailsResponse = apiService.getUserDetails("Bearer ${authResponse.access}")

                        if (userDetailsResponse.isSuccessful) {
                            val userDetails = userDetailsResponse.body()

                            val userId = userDetails?.id_super_user ?: throw Exception("User ID is null")
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

    override suspend fun deleteAccount(): Boolean {
        return try {
            val accessToken = getAccessToken() ?: return false

            val response = apiService.deleteAccount("Bearer $accessToken")

            if (response.isSuccessful) {
                logout()
                true
            } else {
                when (response.code()) {
                    401 -> {
                        val refreshToken = getRefreshToken() ?: return false

                        when (val newTokens = runCatching { refreshTokens(refreshToken) }.getOrNull()) {
                            is AuthTokens -> {
                                apiService.deleteAccount("Bearer ${newTokens.accessToken}")
                                    .isSuccessful
                                    .also { if (it) logout() }
                            }
                            else -> false
                        }
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            false
        }
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
}
