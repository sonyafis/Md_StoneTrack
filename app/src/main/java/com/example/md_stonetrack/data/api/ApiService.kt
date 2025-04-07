package com.example.md_stonetrack.data.api

import com.example.md_stonetrack.data.model.AuthRequest
import com.example.md_stonetrack.data.model.AuthResponse
import com.example.md_stonetrack.data.model.OrderDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("api/orders/")
    suspend fun getOrders(
        @Header("Authorization") token: String
    ): List<OrderDTO>
    @POST("auth/jwt/create/")
    suspend fun login(@Body request: AuthRequest): AuthResponse
}
