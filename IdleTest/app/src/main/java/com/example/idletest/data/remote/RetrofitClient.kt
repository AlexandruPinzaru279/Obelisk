package com.example.idletest.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: GameProgressApi = Retrofit.Builder()
        .baseUrl("https://obelisk-elfv.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GameProgressApi::class.java)
}