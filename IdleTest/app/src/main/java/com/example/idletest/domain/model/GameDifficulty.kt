package com.example.idletest.domain.model

enum class GameDifficulty (
    val enemyHpMultiplier: Double,
    val enemySpeedMultiplier: Double,
    val enemyDamageMultiplier: Double,
    val enemyRewardMultiplier: Double
) {
    EASY(
        enemyHpMultiplier = 0.75,
        enemySpeedMultiplier = 0.9,
        enemyDamageMultiplier = 0.8,
        enemyRewardMultiplier = 1.25
    ),

    NORMAL(
        enemyHpMultiplier = 1.0,
        enemySpeedMultiplier = 1.0,
        enemyDamageMultiplier = 1.0,
        enemyRewardMultiplier = 1.0
    ),

    HARD(
        enemyHpMultiplier = 1.3,
        enemySpeedMultiplier = 1.1,
        enemyDamageMultiplier = 1.25,
        enemyRewardMultiplier = 0.9
    )
}