package com.example.idletest.data.remote

data class GameProgressDto(
    val id: Long? = null,
    val userId: Long = 1,

    val energy: Int = 0,
    val globalEnergy: Int = 0,

    val currentWave: Int = 1,

    val coreHp: Int = 100,
    val coreMaxHp: Int = 100,

    val towerDamage: Int = 10,
    val towerAttackSpeed: Double = 1.0,
    val towerRange: Double = 200.0,

    val upgrades: List<UpgradeProgressDto> = emptyList(),
    val achievements: List<AchievementProgressDto> = emptyList(),
    val abilities: List<AbilityProgressDto> = emptyList()
)