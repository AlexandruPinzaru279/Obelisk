package com.example.idletest.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SessionManager {
    private val _sessionExpiredEvents = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1
    )

    val sessionExpiredEvents = _sessionExpiredEvents.asSharedFlow()

    fun notifySessionExpired(context: Context) {
        AuthStorage.clearSession(context)
        _sessionExpiredEvents.tryEmit(Unit)
    }
}