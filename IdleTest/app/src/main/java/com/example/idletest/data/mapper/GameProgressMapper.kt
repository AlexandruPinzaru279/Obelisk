package com.example.idletest.data.mapper

import com.example.idletest.data.remote.AbilityProgressDto
import com.example.idletest.data.remote.AchievementProgressDto
import com.example.idletest.data.remote.GameProgressDto
import com.example.idletest.data.remote.UpgradeProgressDto
import com.example.idletest.domain.model.Ability
import com.example.idletest.domain.model.Achievement
import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.GameStatus
import com.example.idletest.domain.model.TowerState
import com.example.idletest.domain.model.Upgrade
import com.example.idletest.domain.model.WaveState

// converter class
fun GameState.toDto(
    userId: Long = 1
): GameProgressDto {
    return GameProgressDto(
        userId = userId,

        energy = energy,
        globalEnergy = globalEnergy,

        currentWave = waveState.currentWave,

        coreHp = coreHp,
        coreMaxHp = coreMaxHp,

        towerDamage = tower.damage,
        towerAttackSpeed = tower.attackSpeed,
        towerRange = tower.range,

        upgrades = availableUpgrades.map { upgrade ->
            UpgradeProgressDto(
                id = upgrade.id,
                level = upgrade.level
            )
        },

        achievements = achievements.map { achievement ->
            AchievementProgressDto(
                id = achievement.id,
                unlocked = achievement.unlocked
            )
        },

        abilities = abilities.map { ability ->
            AbilityProgressDto(
                id = ability.id,
                level = ability.level,
                cooldownRemaining = ability.cooldownRemaining
            )
        }
    )
}

fun GameProgressDto.toGameState(): GameState {
    val upgradeLevelsById = upgrades.associateBy(
        keySelector = { it.id },
        valueTransform = { it.level }
    )

    val achievementUnlockedById = achievements.associateBy(
        keySelector = { it.id },
        valueTransform = { it.unlocked }
    )

    val abilityProgressById = abilities.associateBy { ability ->
        ability.id
    }

    val restoredUpgrades = Upgrade.defaultUpgrades().map { upgrade ->
        upgrade.copy(
            level = upgradeLevelsById[upgrade.id] ?: upgrade.level
        )
    }

    val restoredAchievements = Achievement.defaultAchievements().map { achievement ->
        achievement.copy(
            unlocked = achievementUnlockedById[achievement.id] ?: achievement.unlocked
        )
    }

    val restoredAbilities = Ability.defaultAbilities().map { ability ->
        val savedAbility = abilityProgressById[ability.id]

        ability.copy(
            level = savedAbility?.level ?: ability.level,
            cooldownRemaining = savedAbility?.cooldownRemaining ?: ability.cooldownRemaining
        )
    }

    return GameState(
        energy = energy,
        globalEnergy = globalEnergy,

        gameStatus = GameStatus.IDLE,

        waveState = WaveState(
            currentWave = currentWave,
            enemiesToSpawn = currentWave + 1,
            enemiesSpawned = 0,
            enemiesDefeated = 0
        ),

        coreHp = coreHp,
        coreMaxHp = coreMaxHp,

        tower = TowerState(
            damage = towerDamage,
            attackSpeed = towerAttackSpeed,
            attackProgress = 0.0,
            range = towerRange
        ),

        selectedUpgrades = emptyList(),
        availableUpgrades = restoredUpgrades,

        abilities = restoredAbilities,
        enemies = emptyList(),
        achievements = restoredAchievements
    )
}