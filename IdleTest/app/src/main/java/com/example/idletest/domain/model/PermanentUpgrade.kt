package com.example.idletest.domain.model

data class PermanentUpgrade (
    val id: String,
    val name: String,
    val description: String,
    val type: PermanentUpgradeType,
    val level: Int = 0,
    val maxLevel: Int = 10,
    val baseCost: Int,
    val value: Double
) {
    val isMaxLevel: Boolean
        get() = level >= maxLevel

    val currentCost: Int
        get() = (baseCost * (1.0 + level * 0.5)).toInt()

    val currentMultiplier: Double
        get() = 1.0 + level * value

    companion object {
        fun defaultPermanentUpgrades(): List<PermanentUpgrade> {
            return listOf(
                PermanentUpgrade(
                    id = "damage_multiplier",
                    name = "Damage Multiplier",
                    description = "Increases tower damage multiplicatively",
                    type = PermanentUpgradeType.DAMAGE_MULTIPLIER,
                    baseCost = 100,
                    value = 0.1
                ),

                PermanentUpgrade(
                    id = "attack_speed_multiplier",
                    name = "Attack Speed Multiplier",
                    description = "Increases attack speed of the tower",
                    type = PermanentUpgradeType.ATTACK_SPEED_MULTIPLIER,
                    baseCost = 200,
                    value = 0.15
                )
            )
        }
    }
}