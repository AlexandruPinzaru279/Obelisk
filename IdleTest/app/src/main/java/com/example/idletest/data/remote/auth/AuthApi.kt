package com.example.idletest.data.remote.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse
}