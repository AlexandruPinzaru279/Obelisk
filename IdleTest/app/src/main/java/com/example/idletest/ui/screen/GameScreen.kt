package com.example.idletest.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.idletest.data.mapper.toDto
import com.example.idletest.data.mapper.toGameState
import com.example.idletest.data.remote.RetrofitClient
import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.GameStatus
import com.example.idletest.domain.rules.buyUpgrade
import com.example.idletest.domain.rules.combatTick
import com.example.idletest.domain.rules.restartWave
import com.example.idletest.domain.rules.startWave
import com.example.idletest.domain.rules.toggleAutoStartWave
import com.example.idletest.domain.rules.useAbility
import com.example.idletest.ui.components.GameContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameScreen() {
    var gameState by remember {
        mutableStateOf(GameState())
    }

    var message by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()
    val userId = 1L

    // Autoload on launch
    /*
    LaunchedEffect(Unit) {
        try {
            val loadedProgress = RetrofitClient.api.getProgress(userId)
            gameState = loadedProgress.toGameState()
            message = "Progress loaded!"
        } catch (exception: Exception) {
            message = "Load failed, starting with default progress!"
        }
    }
    */

    LaunchedEffect(gameState.gameStatus) {
        while (gameState.gameStatus == GameStatus.IN_WAVE) {
            delay(20.milliseconds)

            var updatedState = gameState.combatTick(
                deltaSeconds = 0.02
            )

            //auto start wave
            if (
                updatedState.gameStatus == GameStatus.WAVE_COMPLETED &&
                updatedState.autoStartWave
            ) {
                updatedState = updatedState.startWave()
            }

            gameState = updatedState
        }
    }

    GameContent(
        gameState = gameState,
        message = message,

        onStartWave = {
            val previousStatus = gameState.gameStatus

            gameState = if (
                previousStatus == GameStatus.DEFEATED
            ) {
                gameState.restartWave()
            } else {
                gameState.startWave()
            }

            message = when {
                previousStatus == GameStatus.DEFEATED -> {
                    "Run restarted."
                }

                previousStatus == GameStatus.IN_WAVE -> {
                    "A wave is already in progress."
                }

                else -> {
                    "Wave ${gameState.waveState.currentWave} started."
                }
            }
        },

        onBuyUpgrade = { upgradeId ->
            val previousEnergy = gameState.energy
            val previousState = gameState

            gameState = gameState.buyUpgrade(upgradeId)

            message = if (gameState == previousState) {
                "Upgrade could not be bought. Not enough energy or max level reached."
            } else {
                "Upgrade bought. Energy: $previousEnergy → ${gameState.energy}"
            }
        },

        onUseAbility = { abilityId ->
            val previousState = gameState
            gameState = gameState.useAbility(abilityId)
            message = if (gameState == previousState) {
                "Ability could not be used!"
            } else {
                "Ability `$abilityId` used"
            }
        },

        onSaveProgress = {
            coroutineScope.launch {
                try {
                    val savedProgress = RetrofitClient.api.saveProgress(
                        userId = userId,
                        progress = gameState.toDto()
                    )

                    gameState = savedProgress.toGameState()
                    message = "Progress saved!"
                } catch (exception: Exception) {
                    message = "Save failed!"
                }
            }
        },

        onLoadProgress = {
            coroutineScope.launch {
                try {
                    val loadedProgress = RetrofitClient.api.getProgress(
                        userId
                    )
                    gameState = loadedProgress.toGameState()
                    message = "Progress loaded!"
                } catch (exception: Exception) {
                    message = "Load failed!"
                }
            }
        },
        onToggleAutoStart = {
            gameState = gameState.toggleAutoStartWave()

            message = if (gameState.autoStartWave) {
                "Auto start enabled."
            } else {
                "Auto start disabled"
            }
        }
    )
}