package com.fisun.md_stonetrack.data.api

import com.fisun.md_stonetrack.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://md-stonetrack.ru/"
    private val authRepository: AuthRepository by inject(AuthRepository::class.java)
    private val tokenMutex = Mutex()

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val token = runBlocking { authRepository.getAccessToken() }
        request.newBuilder()
            .apply { if (!token.isNullOrEmpty()) header("Authorization", "Bearer $token") }.build()
            .let { chain.proceed(it) }
    }

    private val tokenAuthenticator = Authenticator { route, response ->
        if (response.code == 401 && !response.request.url.encodedPath.contains("auth/jwt")) {
            runBlocking {
                tokenMutex.withLock {
                    authRepository.getRefreshToken()?.let { refreshToken ->
                        println("Attempting token refresh...")
                        authRepository.refreshTokens(refreshToken).onSuccess {
                                println("Token refreshed successfully")
                            }.onFailure {
                                println("Token refresh failed: ${it.message}")
                            }.getOrNull()?.accessToken
                    }?.let { newToken ->
                        response.request.newBuilder().header("Authorization", "Bearer $newToken")
                            .build()
                    }
                }
            }
        } else null
    }

    val apiService: ApiService by lazy {
        OkHttpClient.Builder().addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).authenticator(tokenAuthenticator).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
            .let { client ->
                Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(ApiService::class.java)
            }
    }
}