package com.example.idletest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.idletest.data.mapper.toDto
import com.example.idletest.data.mapper.toGameState
import com.example.idletest.data.remote.RetrofitClient
import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.PermanentUpgrade
import com.example.idletest.domain.rules.buyPermanentUpgrade
import kotlinx.coroutines.launch


@Composable
fun PermanentUpgradesScreen(
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var gameState by remember {
        mutableStateOf(GameState())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        try {
            val loadedProgress = RetrofitClient.api.getProgress()

            gameState = loadedProgress.toGameState()
            message = "Permanent upgrades loaded."
        } catch (exception: Exception) {
            message = "Could not load permanent upgrades! Retry in a few minutes"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Permanent Upgrades",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Global energy: ${gameState.globalEnergy}",
            style = MaterialTheme.typography.titleMedium
        )

        if(message.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if(isLoading) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                gameState.permanentUpgrades.forEach { upgrade ->
                    PermanentUpgradeCard(
                        upgrade = upgrade,
                        globalEnergy = gameState.globalEnergy,
                        isSaving = isSaving,
                        onUpgradeClick = {
                            val previousState = gameState
                            val updatedState = gameState.buyPermanentUpgrade(upgrade.id)

                            if (updatedState == previousState) {
                                message = when {
                                    upgrade.isMaxLevel ->
                                        "${upgrade.name} is already at max level."
                                    gameState.globalEnergy < upgrade.currentCost ->
                                        "Not enough global energy"
                                    else -> "The upgrade could no the bought.(bug?)"
                                }
                                return@PermanentUpgradeCard
                            }

                            coroutineScope.launch {
                                isSaving = true
                                message = "Saving upgrade..."

                                try {
                                    val savedProgress = RetrofitClient.api.saveProgress(
                                        progress = updatedState.toDto()
                                    )

                                    gameState = savedProgress.toGameState()
                                    message = "${upgrade.name} upgraded."
                                } catch (exception: Exception) {
                                    gameState = previousState
                                    message = "Upgrade could not be saved."
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving,
            onClick = onBackClick
        ) {
            Text(
                text = "Back"
            )
        }
    }
}

@Composable
private fun PermanentUpgradeCard(
    upgrade: PermanentUpgrade,
    globalEnergy: Int,
    isSaving: Boolean,
    onUpgradeClick: () -> Unit
) {
    val canAfford = globalEnergy >= upgrade.currentCost

    val canBuy =
        !upgrade.isMaxLevel &&
                canAfford &&
                !isSaving

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = upgrade.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = upgrade.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Level: ${upgrade.level}/${upgrade.maxLevel}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "x${formatMultiplier(upgrade.currentMultiplier)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!upgrade.isMaxLevel) {
                Text(
                    text = "Next: x${
                        formatMultiplier(
                            1.0 + (upgrade.level + 1) * upgrade.value
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = canBuy,
                onClick = onUpgradeClick
            ) {
                Text(
                    text = when {
                        upgrade.isMaxLevel -> "MAX LEVEL"
                        isSaving -> "Saving..."
                        else -> "Upgrade - ${upgrade.currentCost}"
                    }
                )
            }
        }
    }
}

private fun formatMultiplier(multiplier: Double): String {
    return String.format("%.2f", multiplier)
}