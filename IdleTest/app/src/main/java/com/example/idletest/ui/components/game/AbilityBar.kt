package com.example.idletest.ui.components.game

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idletest.domain.model.Ability
import com.example.idletest.domain.model.GameStatus

private val Blue = Color(0xFF0984E3)
private val Muted = Color(0xFF636E72)

@Composable
fun AbilityBar(
    abilities: List<Ability>,
    gameStatus: GameStatus,
    onUseAbility: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        abilities.forEach { ability ->
            val ready = ability.cooldownRemaining <= 0.0
            val canUse = ready && gameStatus == GameStatus.IN_WAVE

            OutlinedButton(
                onClick = {
                    onUseAbility(ability.id)
                },
                enabled = canUse,
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(
                    text = if (ready) {
                        ability.name.uppercase()
                    } else {
                        "${ability.name} ${String.format("%.1f", ability.cooldownRemaining)}"
                    },
                    color = if (canUse) Blue else Muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}