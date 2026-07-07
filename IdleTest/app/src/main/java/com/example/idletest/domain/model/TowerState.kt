package com.example.idletest.domain.model

data class TowerState(
    val damage: Int = 40,
    // attacks per second, for simplicity purpose
    val attackSpeed: Double = 1.5,
    // needed for damage calculations inside combatTick()
    val attackProgress: Double = 0.0,
    val range: Double = 100.0,
    // increases the reward gained from enemies and round end
    val rewardMultiplier: Double = 1.0
)
