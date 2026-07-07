package com.example.idletest.domain.rules

import androidx.compose.ui.util.fastCoerceAtMost
import com.example.idletest.domain.model.Ability
import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.GameStatus

fun GameState.useAbility(abilityId: String): GameState {
    if(gameStatus != GameStatus.IN_WAVE) {
        return this
    }

    val ability = abilities.find { it.id == abilityId }
        ?: return this

    // are cooldown
    if (ability.cooldownRemaining > 0.0) {
        return this
    }

    return when (ability.id) {
        "shockwave" -> useShockwave(ability)
        "push" -> usePush(ability)
        "repair" -> useRepair(ability)
        else -> this
    }
}

private fun GameState.useShockwave(ability: Ability): GameState {
    val damage = 50 * ability.level

    val damagedEnemies = enemies.map { enemy ->
        enemy.copy(
            hp = enemy.hp - damage
        )
    }

    val defeatedEnemies = damagedEnemies.filter { enemy ->
        enemy.isDead
    }

    val aliveEnemies = damagedEnemies.filter { enemy ->
        !enemy.isDead
    }

    val reward = defeatedEnemies.sumOf { enemy ->
        enemy.reward
    }

    return copy(
        energy = energy + reward,
        globalEnergy = globalEnergy + reward,
        enemies = aliveEnemies,
        waveState = waveState.copy(
            enemiesDefeated = waveState.enemiesDefeated + defeatedEnemies.size
        ),
        abilities = putAbilityOnCooldown(ability)
    )
}

private fun GameState.useRepair(ability: Ability): GameState{
    if (coreHp >= coreMaxHp) {
        return this
    }

    val healAmount = 25 * ability.level

    // heals to max hp at most
    val updatedCoreHp = (coreHp + healAmount).fastCoerceAtMost(coreMaxHp)

    return copy(
        coreHp = updatedCoreHp,
        abilities = putAbilityOnCooldown(ability)
    )
}

private fun GameState.usePush(ability: Ability): GameState {
    val pushBackDistance = 40.0 * ability.level

    val pushedEnemies = enemies.map { enemy ->
        enemy.copy(
            distanceToCore = enemy.distanceToCore + pushBackDistance
        )
    }

    return copy(
        enemies = pushedEnemies,
        abilities = putAbilityOnCooldown(ability)
    )
}

private fun GameState.putAbilityOnCooldown(usedAbility: Ability): List<Ability> {
    return abilities.map { ability ->
        if (ability.id == usedAbility.id) {
            ability.copy(
                cooldownRemaining = ability.cooldown
            )
        } else {
            ability
        }
    }
}