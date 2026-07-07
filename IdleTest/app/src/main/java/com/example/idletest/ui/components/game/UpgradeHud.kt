package com.example.idletest.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.idletest.domain.model.Upgrade
import com.example.idletest.domain.model.UpgradeType

private val Panel = Color(0xEDE8ECEC)
private val Header = Color(0xFFD5DBDB)
private val Border = Color(0xFF808B8D)
private val Dark = Color(0xFF2D3436)
private val Muted = Color(0xFF636E72)
private val Red = Color(0xFFE74C3C)
private val Blue = Color(0xFF74B9FF)
private val Green = Color(0xFF55E077)
private val Gold = Color(0xFFFFD166)
private val Locked = Color(0xFFB2BEC3)

@Composable
fun UpgradeHud(
    gameState: GameState,
    onBuyUpgrade: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .fillMaxHeight(0.92f)
            .background(Panel, RoundedCornerShape(3.dp))
            .border(1.dp, Border, RoundedCornerShape(3.dp))
    ) {
        Row(
            modifier = Modifier
                .background(Header)
                .padding(start = 5.dp, end = 2.dp, top = 3.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "MODULES",
                    color = Dark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "ENERGY ${gameState.energy}",
                    color = Color(0xFF0984E3),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }

            TextButton(onClick = onClose) {
                Text("–", color = Dark, fontWeight = FontWeight.Black)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SectionTitle("OFFENSIVE (${offensiveLevel(gameState)})")

            gameState.availableUpgrades
                .filter { it.type != UpgradeType.CORE_MAX_HP }
                .forEach { upgrade ->
                    UpgradeModule(
                        upgrade = upgrade,
                        energy = gameState.energy,
                        onBuyUpgrade = onBuyUpgrade
                    )
                }

            Spacer(modifier = Modifier.padding(top = 2.dp))
            SectionTitle("DEFENSIVE (${defensiveLevel(gameState)})")

            gameState.availableUpgrades
                .filter { it.type == UpgradeType.CORE_MAX_HP }
                .forEach { upgrade ->
                    UpgradeModule(
                        upgrade = upgrade,
                        energy = gameState.energy,
                        onBuyUpgrade = onBuyUpgrade
                    )
                }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .background(Color(0xFFBDC3C7), RoundedCornerShape(2.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        color = Dark,
        fontSize = 9.sp,
        fontWeight = FontWeight.Black
    )
}

@Composable
private fun UpgradeModule(
    upgrade: Upgrade,
    energy: Int,
    onBuyUpgrade: (String) -> Unit
) {
    val canBuy = energy >= upgrade.currentCost && !upgrade.isMaxLevel
    val background = when (upgrade.type) {
        UpgradeType.TOWER_DAMAGE -> Red
        UpgradeType.TOWER_ATTACK_SPEED -> Red.copy(alpha = 0.85f)
        UpgradeType.TOWER_RANGE -> Red.copy(alpha = 0.72f)
        UpgradeType.ENEMY_REWARD -> Blue
        UpgradeType.CORE_MAX_HP -> Blue.copy(alpha = 0.78f)
    }

    Column(
        modifier = Modifier
            .background(background, RoundedCornerShape(2.dp))
            .border(1.dp, Color(0x99000000), RoundedCornerShape(2.dp))
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = upgrade.name.uppercase(),
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 8.sp,
                lineHeight = 9.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "${upgrade.level}",
                color = Gold,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
        }

        Text(
            text = upgrade.description,
            color = Color.White.copy(alpha = 0.88f),
            fontSize = 7.sp,
            lineHeight = 8.sp,
            maxLines = 2
        )

        Text(
            text = if (upgrade.isMaxLevel) "MAX LEVEL" else "COST ${upgrade.currentCost}",
            color = if (canBuy) Color.White else Locked,
            fontSize = 7.sp,
            fontWeight = FontWeight.Black
        )

        TextButton(
            onClick = {
                onBuyUpgrade(upgrade.id)
            },
            enabled = canBuy,
            modifier = Modifier
                .background(
                    color = if (canBuy) Green else Locked,
                    shape = RoundedCornerShape(2.dp)
                )
                .padding(horizontal = 2.dp)
        ) {
            Text(
                text = if (upgrade.isMaxLevel) "MAX" else "BUY",
                color = if (canBuy) Dark else Muted,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

private fun offensiveLevel(gameState: GameState): Int {
    return gameState.availableUpgrades
        .filter { upgrade -> upgrade.type != UpgradeType.CORE_MAX_HP }
        .sumOf { upgrade -> upgrade.level }
}

private fun defensiveLevel(gameState: GameState): Int {
    return gameState.availableUpgrades
        .filter { upgrade -> upgrade.type == UpgradeType.CORE_MAX_HP }
        .sumOf { upgrade -> upgrade.level }
}
