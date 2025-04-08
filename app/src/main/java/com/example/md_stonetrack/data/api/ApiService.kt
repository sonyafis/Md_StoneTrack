package com.example.md_stonetrack.data.api

import com.example.md_stonetrack.data.model.AuthRequest
import com.example.md_stonetrack.data.model.AuthResponse
import com.example.md_stonetrack.data.model.OrderDTO
import com.example.md_stonetrack.data.model.RefreshTokenRequest
import com.example.md_stonetrack.data.model.SuperUserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/jwt/create/")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/jwt/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>

    @GET("auth/users/me")
    suspend fun getUserDetails(@Header("Authorization") token: String): Response<SuperUserDTO>

    @GET("api/orders/")
    suspend fun getOrders(@Header("Authorization") token: String): List<OrderDTO>
}
