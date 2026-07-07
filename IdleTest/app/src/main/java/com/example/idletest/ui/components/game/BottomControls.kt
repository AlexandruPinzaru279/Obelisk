package com.example.idletest.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.GameStatus

private val Panel = Color(0xCCEFF3F2)
private val Border = Color(0xFF899597)
private val Red = Color(0xFFD63031)
private val Blue = Color(0xFF0984E3)
private val Green = Color(0xFF00A65A)
private val Dark = Color(0xFF2D3436)
private val Muted = Color(0xFF636E72)

@Composable
fun BottomControls(
    gameState: GameState,
    message: String,
    onStartWave: () -> Unit,
    onUseAbility: (String) -> Unit,
    onSaveProgress: () -> Unit,
    onLoadProgress: () -> Unit,
    onToggleAutoStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(6.dp))
            .border(1.dp, Border, RoundedCornerShape(6.dp))
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                onClick = onStartWave,
                enabled = gameState.gameStatus != GameStatus.IN_WAVE,
                shape = RoundedCornerShape(3.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF95A5A6),
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = when (gameState.gameStatus) {
                        GameStatus.IDLE -> "START"
                        GameStatus.IN_WAVE -> "FIGHT"
                        GameStatus.WAVE_COMPLETED -> "NEXT"
                        GameStatus.DEFEATED -> "RESTART"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Button(
                onClick = onToggleAutoStart,
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(
                    text = if (gameState.autoStartWave) "AUTO: ON" else "AUTO: OFF",
                    color = if(gameState.autoStartWave) Green else Muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }

            AbilityBar(
                abilities = gameState.abilities,
                gameStatus = gameState.gameStatus,
                onUseAbility = onUseAbility,
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = onSaveProgress,
                shape = RoundedCornerShape(3.dp)
            ) {
                Text("SAVE", color = Blue, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }

            OutlinedButton(
                onClick = onLoadProgress,
                shape = RoundedCornerShape(3.dp)
            ) {
                Text("LOAD", color = Blue, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        }

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (message.contains("could not", ignoreCase = true)) Red else Dark,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}
