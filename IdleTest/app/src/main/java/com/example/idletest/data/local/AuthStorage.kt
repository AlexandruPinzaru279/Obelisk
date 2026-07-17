package com.example.idletest.data.local

import android.content.Context

object AuthStorage {
    private const val PREFS_NAME = "obelisk_auth_prefs"
    private const val TOKEN_KEY = "auth_token"
    private const val USERNAME_KEY = "auth_username"

    private fun getPreferences(context: Context) =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    fun saveSession(
        context: Context,
        token: String,
        username: String
    ) {
        getPreferences(context)
            .edit()
            .putString(TOKEN_KEY,token)
            .putString(USERNAME_KEY,username)
            .apply()
    }

    fun getToken(context: Context): String? {
        return getPreferences(context)
            .getString(TOKEN_KEY,null)
    }

    fun getUsername(context: Context): String? {
        return getPreferences(context)
            .getString(USERNAME_KEY,null)
    }

    fun isLoggedIn(context: Context): Boolean {
        return !getToken(context).isNullOrBlank()
    }

    fun clearSession(context: Context) {
        getPreferences(context)
            .edit()
            .clear()
            .apply()
    }
}