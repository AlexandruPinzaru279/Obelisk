package com.example.idletest.backend.dto

data class GameProgressDto(
    val id: Long? = null,
    val userId: Long = 1,

    val energy: Int = 0,
    val globalEnergy: Int = 0,

    val difficulty: String = "NORMAL",

    val currentWave: Int = 1,

    val coreHp: Int = 100,
    val coreMaxHp: Int = 100,

    val towerDamage: Int = 40,
    val towerAttackSpeed: Double = 1.5,
    val towerRange: Double = 200.0,

    val upgrades: List<UpgradeProgressDto> = emptyList(),
    val achievements: List<AchievementProgressDto> = emptyList(),
    val abilities: List<AbilityProgressDto> = emptyList(),
    val permanentUpgrades: List<PermanentUpgradeProgressDto> = emptyList()
)