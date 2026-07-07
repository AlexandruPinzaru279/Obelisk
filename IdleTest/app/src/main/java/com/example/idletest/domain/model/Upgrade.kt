package com.example.idletest.domain.model

data class Upgrade (
    val id: String,
    val name: String,
    val description: String,
    val type: UpgradeType,
    val level: Int = 0,
    val maxLevel: Int = 10,
    val baseCost: Int,
    val value: Double
) {
    val isMaxLevel: Boolean
        get() = level >= maxLevel

    val currentCost: Int
        get() = (baseCost * Math.pow(1.4, level.toDouble())).toInt()

    companion object {
        fun defaultUpgrades(): List<Upgrade> {
            return listOf(
                Upgrade(
                    id = "tower_damage",
                    name = "Tower Damage",
                    description = "increases tower damage",
                    type = UpgradeType.TOWER_DAMAGE,
                    baseCost = 25,
                    value = 5.0
                ),

                Upgrade(
                    id = "tower_attack_speed",
                    name = "Attack Speed",
                    description = "increases how fast the tower attacks",
                    type = UpgradeType.TOWER_ATTACK_SPEED,
                    baseCost = 40,
                    value = 0.15
                ),

                Upgrade(
                    id = "tower_range",
                    name = "Tower Range",
                    description = "Increases tower range",
                    type = UpgradeType.TOWER_RANGE,
                    baseCost = 35,
                    value = 25.0
                ),

                Upgrade(
                    id = "core_max_hp",
                    name = "Core Reinforcement",
                    description = "Increases maximum core health",
                    type = UpgradeType.CORE_MAX_HP,
                    baseCost = 50,
                    value = 20.0
                ),

                Upgrade(
                    id = "enemy_reward",
                    name = "Energy Extractor",
                    description = "Increases energy gained from defeated enemies",
                    type = UpgradeType.ENEMY_REWARD,
                    baseCost = 60,
                    value = 0.50
                )
            )
        }
    }
}