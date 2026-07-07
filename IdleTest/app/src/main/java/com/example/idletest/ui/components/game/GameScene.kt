package com.example.idletest.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idletest.domain.model.GameState
import com.example.idletest.ui.components.battlefield.BattlefieldView

private val Panel = Color(0xAAE7ECEC)
private val PanelBorder = Color(0xFF7F8C8D)
private val DarkText = Color(0xFF2D3436)
private val WaveGreen = Color(0xFF1FA843)
private val ButtonBlue = Color(0xFF2D9CDB)

@Composable
fun GameScene(
    gameState: GameState,
    upgradeHudOpen: Boolean,
    onToggleUpgradeHud: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        BattlefieldView(
            gameState = gameState,
            modifier = Modifier.matchParentSize()
        )

        Text(
            text = "WAVE ${gameState.waveState.currentWave}",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .background(
                    color = Color(0xDDFFFFFF),
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFACB5B7),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 10.dp, vertical = 2.dp),
            color = Color(0xFF2D3436),
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
        )

        Text(
            text = gameState.gameStatus.name,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 38.dp),
            color = WaveGreen,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp
        )

        TextButton(
            onClick = onToggleUpgradeHud,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .background(
                    color = Panel,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(1.dp, PanelBorder, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = if (upgradeHudOpen) "–" else "UPG",
                color = if (upgradeHudOpen) DarkText else ButtonBlue,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )
        }
    }
}
