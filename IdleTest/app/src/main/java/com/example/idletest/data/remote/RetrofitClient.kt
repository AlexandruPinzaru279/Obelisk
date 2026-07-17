package com.example.idletest.data.remote

import android.content.Context
import com.example.idletest.data.remote.auth.AuthApi
import com.example.idletest.data.remote.auth.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "https://obelisk-elfv.onrender.com/"

    lateinit var api: GameProgressApi
        private set

    lateinit var authApi: AuthApi
        private set

    private var initialized = false

    fun initialize(context: Context) {
        if (initialized) {
            return
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(context)
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        api = retrofit.create(
            GameProgressApi::class.java
        )

        authApi = retrofit.create(
            AuthApi::class.java
        )

        initialized = true
    }
}