package com.example.idletest.domain.model

data class GameState (
    // resource for current run progress
    val energy: Int = 0,
    // resource for permanent progress
    val globalEnergy: Int = 0,

    // if the player is in an active wave or in between
    val gameStatus: GameStatus = GameStatus.IDLE,
    // info about the wave ex: nr of enemies to spawn
    val waveState: WaveState = WaveState(),

    val coreHp: Int = 100, // current hp
    val coreMaxHp: Int = 100,
    // tower offensive stats
    val tower: TowerState = TowerState(),

    val autoStartWave: Boolean = false,

    /*
    I want to have a list of upgrades(5 max) that the player
    can select for a run from a list of more upgrades(25 max)
     */
    val selectedUpgrades: List<Upgrade> = emptyList(),
    val availableUpgrades: List<Upgrade> = Upgrade.defaultUpgrades(),

    /* the abilities the player can use to interact with the run
    ex: freeze(stops all enemies for 2 seconds),
    shockwave(damages all nearby enemies)
    repair(gives the tower % missing hp)
     */
    val abilities: List<Ability> = Ability.defaultAbilities(),

    val enemies: List<Enemy> = emptyList(),

    val achievements: List<Achievement> = Achievement.defaultAchievements()
)