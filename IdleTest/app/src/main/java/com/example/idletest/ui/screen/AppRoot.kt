package com.example.idletest.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.idletest.domain.model.GameDifficulty
import com.example.idletest.ui.navigation.AppScreen

// Screen navigation logic!
@Composable
fun AppRoot() {
    var currentScreen by rememberSaveable {
        mutableStateOf(AppScreen.MAIN_MENU)
    }

    var selectedDifficulty by rememberSaveable {
        mutableStateOf(GameDifficulty.NORMAL)
    }

    when(currentScreen) {
        AppScreen.MAIN_MENU -> {
            MainMenuScreen(
                onPlayClick = {
                    currentScreen = AppScreen.DIFFICULTY_SELECT
                },
                onAchievementsClick = {
                    currentScreen = AppScreen.ACHIEVEMENTS
                },
                onPermanentUpgradesClick = {
                    currentScreen = AppScreen.PERMANENT_UPGRADES
                }
            )
        }

        AppScreen.DIFFICULTY_SELECT -> {
            DifficultySelectScreen(
                onDifficultySelected = { difficulty ->
                    selectedDifficulty = difficulty
                    currentScreen = AppScreen.GAME
                },
                onBackClick = {
                    currentScreen = AppScreen.MAIN_MENU
                }
            )
        }

        AppScreen.GAME -> {
            GameScreen(
                difficulty = selectedDifficulty,
                onBackToMenu = {
                    currentScreen = AppScreen.MAIN_MENU
                }
            )
        }

        AppScreen.ACHIEVEMENTS -> {
            AchievementsScreen(
                onBackClick = {
                    currentScreen = AppScreen.MAIN_MENU
                }
            )
        }

        AppScreen.PERMANENT_UPGRADES -> {
            PermanentUpgradesScreen(
                onBackClick = {
                    currentScreen = AppScreen.MAIN_MENU
                }
            )
        }
    }
}