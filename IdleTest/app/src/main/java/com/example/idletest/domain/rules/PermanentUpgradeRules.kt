package com.example.idletest.domain.rules

import com.example.idletest.domain.model.GameState

fun GameState.buyPermanentUpgrade(upgradeId: String): GameState {
    val upgrade = permanentUpgrades.find {it.id == upgradeId}
        ?: return this

    if (upgrade.isMaxLevel) {
        return this
    }

    if (globalEnergy < upgrade.currentCost) {
        return this
    }

    val upgraded = upgrade.copy(
        level = upgrade.level + 1
    )

    val updatedUpgrades = permanentUpgrades.map { currentUpgrade ->
        if(currentUpgrade.id == upgradeId) {
            upgraded
        } else {
            currentUpgrade
        }
    }

    return copy(
        globalEnergy = globalEnergy - upgrade.currentCost,
        permanentUpgrades = updatedUpgrades
    )
}