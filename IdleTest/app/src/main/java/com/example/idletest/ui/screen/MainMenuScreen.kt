package com.example.idletest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    onContinueClick: () -> Unit,
    onNewGameClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onPermanentUpgradesClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Obelisk",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        // continue button
        Button(
            modifier = Modifier.width(240.dp),
            onClick = onContinueClick
        ) {
            Text(text = "Continue")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // new game button
        Button(
            modifier = Modifier.width(240.dp),
            onClick = onNewGameClick
        ) {
            Text(text = "New Game")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // achievements button
        Button(
            modifier = Modifier.width(240.dp),
            onClick = onAchievementsClick
        ) {
            Text(
                text = "Achievements"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.width(240.dp),
            onClick = onPermanentUpgradesClick
        ) {
            Text(
                text = "Upgrades"
            )
        }
    }
}