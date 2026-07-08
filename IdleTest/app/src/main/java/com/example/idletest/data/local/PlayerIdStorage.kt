package com.example.idletest.data.local

import android.content.Context
import java.util.UUID
import kotlin.math.abs

object PlayerIdStorage {
    private const val PREFS_NAME = "obelisk_player_prefs"
    private const val PLAYER_ID_KEY = "player_id"

    fun getOrCreatePlayerId(context: Context): Long {
        val preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val savedPlayerId = preferences.getLong(
            PLAYER_ID_KEY,
            -1L
        )

        if(savedPlayerId != -1L) {
            return savedPlayerId
        }

        val newPlayerId = abs(UUID.randomUUID().mostSignificantBits)

        preferences.edit().putLong(PLAYER_ID_KEY,newPlayerId).apply()

        return newPlayerId
    }
}