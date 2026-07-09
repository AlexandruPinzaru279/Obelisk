package com.example.idletest.domain.rules

import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.Achievement

// extension function
fun GameState.withUpdatedAchievements(): GameState {
    val updatedAchievements = achievements.map { achievement ->
        if(achievement.unlocked) {
            achievement
        } else {
            achievement.copy(
                unlocked = shouldUnlockAchievement(achievement.id)
            )
        }
    }
    return this.copy(
        achievements = updatedAchievements
    )
}

private fun GameState.shouldUnlockAchievement(
    achievementId: String
): Boolean {
    return when(achievementId) {
        "first_wave" -> waveState.currentWave > 1

        "wave_5" -> waveState.currentWave >= 5

        "wave_10" -> waveState.currentWave >= 10

        "first_energy" -> globalEnergy >= 100

        "energy_collector" -> globalEnergy >= 1_000

        "core_survivor" -> coreHp == coreMaxHp && waveState.currentWave >= 3

        "tower_upgraded" -> availableUpgrades.any { upgrade ->
            upgrade.level > 0
        }

        "tower_builder" -> availableUpgrades.count { upgrade ->
            upgrade.level > 0
        } >= 3

        else -> false
    }
}