package com.fisun.md_stonetrack.data.api

import com.fisun.md_stonetrack.data.model.AuthRequest
import com.fisun.md_stonetrack.data.model.AuthResponse
import com.fisun.md_stonetrack.data.model.ChangePasswordRequest
import com.fisun.md_stonetrack.data.model.FeedbackDTO
import com.fisun.md_stonetrack.data.model.OrderDTO
import com.fisun.md_stonetrack.data.model.RefreshTokenRequest
import com.fisun.md_stonetrack.data.model.ResetPasswordRequest
import com.fisun.md_stonetrack.data.model.SuperUserDTO
import com.fisun.md_stonetrack.domain.model.RegistrationRequest
import com.fisun.md_stonetrack.domain.model.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @DELETE("api/users/delete")
    suspend fun deleteAccount(
        @Header("Authorization") token: String
    ): Response<Unit>
    @POST("auth/users/set_password/")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<Unit>

    @POST("auth/users/reset_password/")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>
}
