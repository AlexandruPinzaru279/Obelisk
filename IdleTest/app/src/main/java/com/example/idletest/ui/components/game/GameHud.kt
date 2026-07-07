package com.example.idletest.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idletest.domain.model.GameState

private val Panel = Color(0xDDEDF1F1)
private val Border = Color(0xFF98A3A5)
private val Dark = Color(0xFF2D3436)
private val Green = Color(0xFF1FA843)
private val Blue = Color(0xFF2D9CDB)
private val Gold = Color(0xFFE2A218)
private val Red = Color(0xFFD63031)

@Composable
fun GameHud(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Panel, RoundedCornerShape(5.dp))
            .border(1.dp, Border, RoundedCornerShape(5.dp))
            .padding(horizontal = 7.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        CompactHudStat(label = "XP", value = compactNumber(gameState.globalEnergy), color = Blue)
        CompactHudStat(label = "$", value = compactNumber(gameState.energy), color = Gold)
        CompactHudStat(label = "CORE", value = "${gameState.coreHp}/${gameState.coreMaxHp}", color = if (gameState.coreHp > gameState.coreMaxHp * 0.35) Green else Red)
        CompactHudStat(label = "ENM", value = gameState.enemies.size.toString(), color = Red)
    }
}

@Composable
private fun CompactHudStat(
    label: String,
    value: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            color = Dark.copy(alpha = 0.72f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            text = value,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black
        )
    }
}

private fun compactNumber(value: Int): String {
    return when {
        value >= 1_000_000_000 -> "${value / 1_000_000_000}B"
        value >= 1_000_000 -> "${value / 1_000_000}M"
        value >= 1_000 -> "${value / 1_000}K"
        else -> value.toString()
    }
}
