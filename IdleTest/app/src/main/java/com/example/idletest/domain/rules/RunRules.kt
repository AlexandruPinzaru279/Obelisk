package com.example.idletest.domain.rules

import com.example.idletest.domain.model.Ability
import com.example.idletest.domain.model.GameDifficulty
import com.example.idletest.domain.model.GameState

fun GameState.startNewRun(
    selectedDifficulty: GameDifficulty
): GameState {
    val resetAbilities = Ability.defaultAbilities().map { defaultAbility ->
        val currentAbility = abilities.find { ability ->
            ability.id == defaultAbility.id
        }

        defaultAbility.copy(
            level = currentAbility?.level ?: defaultAbility.level,
            cooldownRemaining = 0.0
        )
    }

    return GameState(
        globalEnergy = globalEnergy,
        difficulty = selectedDifficulty,

        permanentUpgrades = permanentUpgrades,
        achievements = achievements,
        abilities = resetAbilities
    )
}