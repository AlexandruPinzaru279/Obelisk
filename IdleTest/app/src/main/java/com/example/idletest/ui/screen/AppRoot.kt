package com.example.idletest.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.idletest.domain.model.GameDifficulty
import com.example.idletest.ui.navigation.AppScreen
import com.example.idletest.ui.navigation.GameLaunchMode

// Screen navigation logic!
@Composable
fun AppRoot() {
    var currentScreen by rememberSaveable {
        mutableStateOf(AppScreen.MAIN_MENU)
    }

    var selectedDifficulty by rememberSaveable {
        mutableStateOf(GameDifficulty.NORMAL)
    }

    var gameLaunchMode by rememberSaveable {
        mutableStateOf(GameLaunchMode.CONTINUE)
    }

    when(currentScreen) {
        AppScreen.MAIN_MENU -> {
            MainMenuScreen(
                onContinueClick = {
                    gameLaunchMode = GameLaunchMode.CONTINUE
                    currentScreen = AppScreen.GAME
                },
                onNewGameClick = {
                    gameLaunchMode = GameLaunchMode.NEW_GAME
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
                    gameLaunchMode = GameLaunchMode.NEW_GAME
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
                launchMode = gameLaunchMode,
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