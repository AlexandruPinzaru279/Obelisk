package com.example.idletest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.idletest.data.local.PlayerIdStorage
import com.example.idletest.data.mapper.toGameState
import com.example.idletest.data.remote.RetrofitClient
import com.example.idletest.domain.model.Achievement
import com.example.idletest.domain.model.GameState

@Composable
fun AchievementsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val userId = remember {
        PlayerIdStorage.getOrCreatePlayerId(context)
    }

    var gameState by remember {
        mutableStateOf(GameState())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var message by remember {
        mutableStateOf("")
    }

    LaunchedEffect(userId) {
        try {
            val loadedProgress = RetrofitClient.api.getProgress(userId)
            gameState = loadedProgress.toGameState()
            message = "Achievements loaded."
        } catch (exception: Exception) {
            message = "Could not load achievements. Showing default state"
        } finally {
            isLoading = false
        }
    }

    val unlockedCount = gameState.achievements.count { achievement ->
        achievement.unlocked
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
        Text(
            text = "Achievements",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$unlockedCount / ${gameState.achievements.size} unlocked",
            style = MaterialTheme.typography.bodyLarge
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
                modifier = Modifier.fillMaxSize().weight(1f),
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                gameState.achievements.forEach { achievement ->
                    AchievementRow(
                        achievement = achievement
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBackClick
        ) {
            Text(
                text = "Back"
            )
        }
    }
}

@Composable
private fun AchievementRow(
    achievement: Achievement
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (achievement.unlocked) "✓" else "□",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (achievement.unlocked) "Unlocked" else "Locked",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}