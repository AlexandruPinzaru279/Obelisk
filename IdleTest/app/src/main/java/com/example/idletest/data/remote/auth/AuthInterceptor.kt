package com.example.idletest.data.remote.auth

import android.content.Context
import com.example.idletest.data.local.AuthStorage
import com.example.idletest.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    context: Context
) : Interceptor {

    private val applicationContext =
        context.applicationContext

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val originalRequest = chain.request()

        val isAuthRequest =
            originalRequest.url().encodedPath()
                .startsWith("/api/auth/")

        val token =
            AuthStorage.getToken(applicationContext)

        val requestBuilder =
            originalRequest.newBuilder()

        if (!isAuthRequest && !token.isNullOrBlank()) {
            requestBuilder.header(
                "Authorization",
                "Bearer $token"
            )
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        if (response.code() == 401 && !isAuthRequest) {
            SessionManager.notifySessionExpired(
                applicationContext
            )
        }

        return response
    }
}