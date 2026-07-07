package com.example.idletest.domain.rules

import com.example.idletest.domain.model.Enemy
import com.example.idletest.domain.model.EnemyType
import com.example.idletest.domain.model.GameState
import com.example.idletest.domain.model.GameStatus
import com.example.idletest.domain.model.UpgradeType
import kotlin.random.Random

fun GameState.startWave(): GameState {
    if(gameStatus == GameStatus.IN_WAVE) {
        return this
        // nu se intampla nimic pentru ca esti deja in wave
    }

    if(gameStatus == GameStatus.DEFEATED) {
        return this
        // nu ar trebui sa poti face nimic
    }

    return copy(
        gameStatus = GameStatus.IN_WAVE,
        waveState = waveState.copy(
            enemiesSpawned = 0,
            enemiesDefeated = 0,
            spawnTimer = 0.0
        ),
        enemies = emptyList()
    )
}

// restart wave with full heal and abilities
fun GameState.restartWave(): GameState {
    if (gameStatus != GameStatus.DEFEATED) {
        return this
    }

    val resetAbilities = abilities.map { ability ->
        ability.copy(
            cooldownRemaining = 0.0
        )
    }

    return copy(
        gameStatus = GameStatus.IDLE,
        coreHp = coreMaxHp,
        enemies = emptyList(),
        abilities = resetAbilities,
        waveState = waveState.copy(
            enemiesSpawned = 0,
            enemiesDefeated = 0,
            spawnTimer = 0.0
        ),
        tower = tower.copy(
            attackProgress = 0.0
        )
    )
}

fun GameState.toggleAutoStartWave(): GameState {
    return copy(
        autoStartWave = !autoStartWave
    )
}

fun GameState.completeWave(): GameState {
    if(gameStatus != GameStatus.IN_WAVE) {
        return this // you need to be in wave to complete it
    }

    val nextWave = waveState.currentWave + 1
    val nextEnemiesToSpawn = waveState.enemiesToSpawn + 1
    val waveReward = waveState.currentWave * 20

    return copy(
        gameStatus = GameStatus.WAVE_COMPLETED,
        energy = energy + waveReward,
        globalEnergy = globalEnergy + waveReward,
        waveState = waveState.copy(
            currentWave = nextWave,
            enemiesToSpawn = nextEnemiesToSpawn,
            enemiesSpawned = 0,
            enemiesDefeated = 0,
            spawnTimer = 0.0
        ),
        enemies = emptyList()
    )
}

fun GameState.combatTick(deltaSeconds: Double): GameState {
    if(gameStatus != GameStatus.IN_WAVE) {
        return this
    }

    var updatedWaveState = waveState
    var currentEnemies = enemies

    val shouldSpawnEnemy =
        updatedWaveState.enemiesSpawned < updatedWaveState.enemiesToSpawn &&
        updatedWaveState.spawnTimer <= 0.0

    if(shouldSpawnEnemy) {
        val nextEnemyIndex = updatedWaveState.enemiesSpawned + 1
        val isBossWave = updatedWaveState.currentWave % 5 == 0
        val isLastEnemy = updatedWaveState.enemiesSpawned == updatedWaveState.enemiesToSpawn - 1


        // random spawn logic
        val enemyType = if (isBossWave && isLastEnemy) {
            EnemyType.BOSS
        } else if(isBossWave) {
            listOf(
                EnemyType.BASIC,
                EnemyType.FAST
            ).random()
        } else {
            listOf(
                EnemyType.BASIC,
                EnemyType.BASIC,
                EnemyType.FAST,
                EnemyType.TANK
            ).random()
        }

        val newEnemy = Enemy.createForWave(
            wave = updatedWaveState.currentWave,
            index = nextEnemyIndex,
            type = enemyType
        )

        currentEnemies = currentEnemies + newEnemy

        updatedWaveState = updatedWaveState.copy(
            enemiesSpawned = nextEnemyIndex,
            spawnTimer = updatedWaveState.spawnInterval
        )
    } else {
        updatedWaveState = updatedWaveState.copy(
            spawnTimer = updatedWaveState.spawnTimer - deltaSeconds
        )
    }

    val movedEnemies = currentEnemies.map { enemy ->
        enemy.copy(
            distanceToCore = enemy.distanceToCore - enemy.speed * deltaSeconds
        )
    }

    val enemiesThatReachedCore = movedEnemies.filter { enemy ->
        enemy.distanceToCore <= 0
    }

    val coreDamage = enemiesThatReachedCore.sumOf { enemy ->
        enemy.damage
    }

    var enemiesStillOnPath = movedEnemies.filter { enemy ->
        enemy.distanceToCore > 0
    }

    val newAttackProgress = tower.attackProgress + tower.attackSpeed * deltaSeconds
    val attacksToPerform = newAttackProgress.toInt()
    val remainingAttackProgress = newAttackProgress - attacksToPerform

    repeat(times = attacksToPerform) {
        val target = enemiesStillOnPath . filter { enemy ->
            enemy.distanceToCore <= tower.range
        } .minByOrNull { enemy ->
            enemy.distanceToCore
        }

        if (target != null) {
            enemiesStillOnPath = enemiesStillOnPath.map { enemy ->
                if(enemy.id == target.id) {
                    enemy.copy(
                        hp = enemy.hp - tower.damage
                    )
                } else {
                    enemy
                }
            }
        }
    }

    val defeatedEnemies = enemiesStillOnPath.filter { enemy ->
        enemy.isDead
    }

    val aliveEnemies = enemiesStillOnPath.filter { enemy ->
        !enemy.isDead
    }

    val reward = defeatedEnemies.sumOf { enemy ->
        enemy.reward
    } * tower.rewardMultiplier

    val newCoreHp = (coreHp - coreDamage).coerceAtLeast(minimumValue = 0)

    // lowers the cooldown of abilities every tick
    val updatedAbilities = abilities.map { ability ->
        if(ability.cooldownRemaining > 0.0) {
            ability.copy(
                cooldownRemaining = (ability.cooldownRemaining - deltaSeconds).coerceAtLeast(0.0)
            )
        } else {
            ability
        }
    }

    val updatedState = copy(
        energy = (energy + reward).toInt(),
        globalEnergy = (globalEnergy + reward).toInt(),
        coreHp = newCoreHp,
        tower = tower.copy(
            attackProgress = remainingAttackProgress
        ),
        waveState = updatedWaveState.copy(
            enemiesDefeated = waveState.enemiesDefeated + defeatedEnemies.size
        ),
        enemies = aliveEnemies,
        abilities = updatedAbilities
    )

    if(newCoreHp <= 0) {
        return updatedState.copy(
            gameStatus = GameStatus.DEFEATED
        )
    }

    val allEnemiesSpawned =
        updatedState.waveState.enemiesSpawned >= updatedState.waveState.enemiesToSpawn

    if(aliveEnemies.isEmpty() && allEnemiesSpawned) {
        return updatedState.completeWave()
    }

    return updatedState
}



fun GameState.buyUpgrade(upgradeId: String): GameState {
    val upgrade = availableUpgrades.find { it.id == upgradeId }
        ?: return this

    // the upgrade is already at max level
    if(upgrade.isMaxLevel) {
        return this
    }

    val cost = upgrade.currentCost

    // not enough resources to buy the upgrade
    if(energy < upgrade.currentCost) {
        return this
    }

    val upgraded = upgrade.copy(
        level = upgrade.level + 1
    )

    val updatedUpgrades = availableUpgrades.map { currentUpgrade ->
        if(currentUpgrade.id == upgradeId) {
            upgraded
        } else {
            currentUpgrade
        }
    }

    var updatedTower = tower
    var updatedCoreHp = coreHp
    var updatedCoreMaxHp = coreMaxHp

    when (upgrade.type) {
        UpgradeType.TOWER_DAMAGE -> {
            updatedTower = tower.copy(
                damage = tower.damage + upgrade.value.toInt()
            )
        }

        UpgradeType.TOWER_ATTACK_SPEED -> {
            updatedTower = tower.copy(
                attackSpeed = tower.attackSpeed + upgrade.value
            )
        }

        UpgradeType.TOWER_RANGE -> {
            updatedTower = tower.copy(
                range = tower.range + upgrade.value
            )
        }

        UpgradeType.CORE_MAX_HP -> {
            val hpIncrease = upgrade.value.toInt()

            updatedCoreMaxHp = coreMaxHp + hpIncrease
            updatedCoreHp = coreHp + hpIncrease
        }

        UpgradeType.ENEMY_REWARD -> {
            updatedTower = tower.copy(
                rewardMultiplier = tower.rewardMultiplier + upgrade.value
            )
        }
    }

    return copy(
        energy = energy - cost,
        tower = updatedTower,
        coreHp = updatedCoreHp,
        coreMaxHp = updatedCoreMaxHp,
        availableUpgrades = updatedUpgrades
    )
}