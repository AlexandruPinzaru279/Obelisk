package com.example.idletest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.idletest.domain.model.GameState
import com.example.idletest.ui.components.game.BottomControls
import com.example.idletest.ui.components.game.GameHud
import com.example.idletest.ui.components.game.GameScene
import com.example.idletest.ui.components.game.UpgradeHud

@Composable
fun GameContent(
    gameState: GameState,
    message: String,
    onStartWave: () -> Unit,
    onBuyUpgrade: (String) -> Unit,
    onUseAbility: (String) -> Unit,
    onSaveProgress: () -> Unit,
    onLoadProgress: () -> Unit,
    onToggleAutoStart: () -> Unit
) {
    var upgradeHudOpen by rememberSaveable {
        mutableStateOf(true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6BC04A))
    ) {
        GameScene(
            gameState = gameState,
            upgradeHudOpen = upgradeHudOpen,
            onToggleUpgradeHud = {
                upgradeHudOpen = !upgradeHudOpen
            },
            modifier = Modifier.fillMaxSize()
        )

        GameHud(
            gameState = gameState,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = if (upgradeHudOpen) 140.dp else 8.dp, top = 6.dp, end = 58.dp)
        )

        if (upgradeHudOpen) {
            UpgradeHud(
                gameState = gameState,
                onBuyUpgrade = onBuyUpgrade,
                onClose = {
                    upgradeHudOpen = false
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
            )
        }

        BottomControls(
            gameState = gameState,
            message = message,
            onStartWave = onStartWave,
            onUseAbility = onUseAbility,
            onSaveProgress = onSaveProgress,
            onLoadProgress = onLoadProgress,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            onToggleAutoStart = onToggleAutoStart
        )
    }
}
