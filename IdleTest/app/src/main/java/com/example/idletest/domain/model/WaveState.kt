package com.example.idletest.domain.model

data class WaveState (
    val currentWave: Int = 1,
    val enemiesToSpawn: Int = 2,
    val enemiesSpawned: Int = 0,
    val enemiesDefeated: Int = 0,
    val spawnTimer: Double = 0.0, // time until the next enemy will spawn
    val spawnInterval: Double = 0.5 // self-explanatory
)