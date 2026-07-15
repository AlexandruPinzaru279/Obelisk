package com.example.idletest.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class GameProgress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var userId: Long = 1,

    var energy: Int = 0,
    var globalEnergy: Int = 0,

    @Column(length = 20)
    var difficulty: String? = "NORMAL",

    var currentWave: Int = 1,

    var coreHp: Int = 100,
    var coreMaxHp: Int = 100,

    var towerDamage: Int = 40,
    var towerAttackSpeed: Double = 1.5,
    var towerRange: Double = 200.0,

    @Column(columnDefinition = "TEXT")
    var upgradesJson: String = "[]",

    @Column(columnDefinition = "TEXT")
    var achievementsJson: String = "[]",

    @Column(columnDefinition = "TEXT")
    var abilitiesJson: String = "[]",

    @Column(columnDefinition = "TEXT")
    var permanentUpgradesJson: String = "[]"
)
