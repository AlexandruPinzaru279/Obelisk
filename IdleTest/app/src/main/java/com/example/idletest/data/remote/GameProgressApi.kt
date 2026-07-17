package com.example.idletest.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface GameProgressApi {
    @GET("api/progress/me")
    suspend fun getProgress(): GameProgressDto

    @PUT("api/progress/me")
    suspend fun saveProgress(
        @Body progress: GameProgressDto
    ): GameProgressDto
}