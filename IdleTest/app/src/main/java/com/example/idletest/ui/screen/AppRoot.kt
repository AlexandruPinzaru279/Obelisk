package com.example.idletest.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.idletest.data.local.AuthStorage
import com.example.idletest.data.local.SessionManager
import com.example.idletest.domain.model.GameDifficulty
import com.example.idletest.ui.navigation.AppScreen
import com.example.idletest.ui.navigation.GameLaunchMode

// Screen navigation logic!
@Composable
fun AppRoot() {
    val context = LocalContext.current

    var currentScreen by rememberSaveable {
        mutableStateOf(
            if (AuthStorage.isLoggedIn(context)) {
                AppScreen.MAIN_MENU
            } else {
                AppScreen.LOGIN
            }
        )
    }

    var selectedDifficulty by rememberSaveable {
        mutableStateOf(GameDifficulty.NORMAL)
    }

    var gameLaunchMode by rememberSaveable {
        mutableStateOf(GameLaunchMode.CONTINUE)
    }

    var loginMessage by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        SessionManager.sessionExpiredEvents.collect {
            loginMessage =
                "Sesiunea a expirat. Autentifica-te din nou."

            currentScreen = AppScreen.LOGIN
        }
    }

    when (currentScreen) {
        AppScreen.LOGIN -> {
            LoginScreen(
                initialMessage = loginMessage,
                onLoginSuccess = {
                    loginMessage = null
                    currentScreen = AppScreen.MAIN_MENU
                },
                onRegisterClick = {
                    loginMessage = null
                    currentScreen = AppScreen.REGISTER
                }
            )
        }

        AppScreen.REGISTER -> {
            RegisterScreen(
                onRegisterSuccess = {
                    currentScreen = AppScreen.MAIN_MENU
                },
                onBackToLogin = {
                    currentScreen = AppScreen.LOGIN
                }
            )
        }

        AppScreen.MAIN_MENU -> {
            MainMenuScreen(
                username = AuthStorage.getUsername(context) ?: "Player",
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
                },
                onLogoutClick = {
                    loginMessage = null
                    AuthStorage.clearSession(context)
                    currentScreen = AppScreen.LOGIN
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