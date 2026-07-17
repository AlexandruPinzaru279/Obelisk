package com.example.idletest.data.remote.auth

import android.content.Context
import com.example.idletest.data.local.AuthStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    context: Context
): Interceptor {
    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = AuthStorage.getToken(applicationContext)

        val requestBuilder = chain
            .request()
            .newBuilder()

        if(!token.isNullOrBlank()) {
            requestBuilder.header(
                "Authorization",
                "Bearer $token"
            )
        }

        return chain.proceed(
            requestBuilder.build()
        )
    }
}