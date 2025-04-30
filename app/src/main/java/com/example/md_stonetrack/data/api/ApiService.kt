package com.example.md_stonetrack.data.api

import com.example.md_stonetrack.data.model.AuthRequest
import com.example.md_stonetrack.data.model.AuthResponse
import com.example.md_stonetrack.data.model.FeedbackDTO
import com.example.md_stonetrack.data.model.OrderDTO
import com.example.md_stonetrack.data.model.RefreshTokenRequest
import com.example.md_stonetrack.data.model.SuperUserDTO
import com.example.md_stonetrack.domain.model.RegistrationRequest
import com.example.md_stonetrack.domain.model.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/jwt/create/")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/jwt/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>

    @GET("auth/users/me")
    suspend fun getUserDetails(@Header("Authorization") token: String): Response<SuperUserDTO>

    @GET("api/orders/")
    suspend fun getOrders(@Header("Authorization") token: String): List<OrderDTO>
    @POST("api/feedbacks/")
    suspend fun sendFeedback(@Header("Authorization") token: String, @Body feedback: FeedbackDTO)

    @POST("auth/users/")
    suspend fun registerUser(@Body request: RegistrationRequest): Response<RegistrationResponse>

    @GET("auth/users/check_username/")
    suspend fun checkUsernameExists(@Query("username") username: String): Response<Boolean>

    @GET("auth/users/check_email/")
    suspend fun checkEmailExists(@Query("email") email: String): Response<Boolean>
    @PATCH("api/orders/{id}/")
    suspend fun updateOrderStatus(
        @Path("id") id: Int,
        @Body statusUpdate: Map<String, Int>
    )

}
