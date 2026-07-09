package com.example.idletest.ui.screen

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.idletest.domain.model.GameDifficulty
import java.nio.file.WatchEvent

//
@Composable
fun DifficultySelectScreen(
    onDifficultySelected: (GameDifficulty) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Difficulty",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            modifier = Modifier.width(240.dp),
            onClick = {
                onDifficultySelected(GameDifficulty.EASY)
            }
        ) {
            Text(
                text = "Easy"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.width(240.dp),
            onClick = {
                onDifficultySelected(GameDifficulty.NORMAL)
            }
        ) {
            Text(
                text = "Normal"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.width(240.dp),
            onClick = {
                onDifficultySelected(GameDifficulty.HARD)
            }
        ) {
            Text(
                text = "Hard"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.width(240.dp),
            onClick = onBackClick
        ) {
            Text(
                text = "Back"
            )
        }
    }
}