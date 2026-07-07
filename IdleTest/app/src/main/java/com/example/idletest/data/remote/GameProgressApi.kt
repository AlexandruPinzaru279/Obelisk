package com.example.idletest.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface GameProgressApi {
    @GET("api/progress/{userId}")
    suspend fun getProgress(@Path("userId") userId: Long): GameProgressDto

    @PUT("api/progress/{userId}")
    suspend fun saveProgress(
        @Path("userId") userId: Long,
        @Body progress: GameProgressDto
    ): GameProgressDto
}