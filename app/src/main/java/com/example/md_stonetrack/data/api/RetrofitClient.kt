package com.example.md_stonetrack.data.api

import android.R.attr.level
import com.example.md_stonetrack.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val authRepository: AuthRepository by inject(AuthRepository::class.java)

    private class TokenAuthenticator : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            // Не пытаемся обновлять токен для эндпоинтов аутентификации
            if (response.request.url.encodedPath.contains("auth/jwt")) {
                return null
            }

            if (response.code == 401 && response.request.header("Authorization") != null) {
                return runBlocking {
                    try {
                        val refresh = authRepository.getRefreshToken()
                        if (!refresh.isNullOrEmpty()) {
                            val result = authRepository.refreshTokens(refresh)
                            when {
                                result.isSuccess -> {
                                    val newAccessToken = result.getOrNull()?.accessToken
                                    if (!newAccessToken.isNullOrEmpty()) {
                                        return@runBlocking response.request.newBuilder()
                                            .header("Authorization", "Bearer $newAccessToken")
                                            .build()
                                    }
                                }
                                result.exceptionOrNull()?.message?.contains("Session expired") == true -> {
                                    // Здесь можно вызвать logout или уведомить UI о необходимости входа
                                    return@runBlocking null
                                }
                            }
                        }
                        null
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            return null
        }
    }

    val apiService: ApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .authenticator(TokenAuthenticator())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}