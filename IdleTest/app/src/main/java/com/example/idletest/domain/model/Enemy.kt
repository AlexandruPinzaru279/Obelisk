package com.example.idletest.domain.model

data class Enemy(
    val id: String,
    val type: EnemyType,
    val hp: Int,
    val maxHp: Int,
    val damage: Int,
    val speed: Double, // units per second
    val distanceToCore: Double, // units
    val reward: Int // amount of energy the player gets when defeating the enemy
    // could add another val for defense, and add pierce for tower
    // val defense: Int
) {
    val isDead: Boolean
        get() = hp <=0

    companion object {
        fun createForWave(
            wave: Int,
            index: Int,
            type: EnemyType = EnemyType.BASIC
        ): Enemy {
            return when (type) {
                EnemyType.BASIC -> Enemy(
                    id = "wave_${wave}_enemy_$index",
                    type = EnemyType.BASIC,
                    hp = 30 + wave * 5,
                    maxHp = 30 + wave * 5,
                    damage = 5 + wave,
                    speed = 40.0,
                    distanceToCore = 300.0,
                    reward = 5 + wave
                )

                EnemyType.FAST -> Enemy(
                    id = "wave_${wave}_enemy_$index",
                    type = EnemyType.FAST,
                    hp = 18 + wave * 3,
                    maxHp = 18 + wave * 3,
                    damage = 4 + wave,
                    speed = 70.0,
                    distanceToCore = 300.0,
                    reward = 6 + wave
                )
                EnemyType.TANK -> Enemy(
                    id = "wave_${wave}_enemy_$index",
                    type = EnemyType.TANK,
                    hp = 80 + wave * 10,
                    maxHp = 80 + wave * 10,
                    damage = 10 + wave,
                    speed =25.0,
                    distanceToCore = 300.0,
                    reward = 12 + wave * 2
                )
                EnemyType.BOSS -> Enemy(
                    id = "wave_${wave}_enemy_$index",
                    type = EnemyType.BOSS,
                    hp = 250 + wave * 40,
                    maxHp = 250 + wave * 40,
                    damage = 20 + wave * 2,
                    speed = 20.0,
                    distanceToCore = 300.0,
                    reward = 50  + wave * 5
                )
            }
        }
    }
}