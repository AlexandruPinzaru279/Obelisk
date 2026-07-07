package com.example.idletest.domain.model

data class Achievement (
    val id: String,
    val title: String,
    val description: String,
    val unlocked: Boolean = false
) {
    companion object {
        fun defaultAchievements(): List<Achievement> {
            return listOf(
                Achievement(
                    id = "first_wave",
                    title = "First Wave",
                    description = "Defeat your first wave."
                ), // ADD MORE!
                Achievement(
                    id = "wave_5",
                    title = "Fifth Wave",
                    description = "Defeat wave 5."
                ),
                Achievement(
                    id = "wave_10",
                    title = "Tenth Wave",
                    description = "Defeat wave 10."
                ),
                Achievement(
                    id = "first_energy",
                    title = "First Energy",
                    description = "Reach 100 global energy."
                ),
                Achievement(
                    id = "energy_collector",
                    title = "Energy Collector",
                    description = "Reach 1000 global energy."
                ),
                Achievement(
                    id = "core_survivor",
                    title = "Core Survivor",
                    description = "Reach wave 3 without taking any damage."
                ),
                Achievement(
                    id = "tower_upgraded",
                    title = "Tower Upgraded",
                    description = "Buy your first upgrade."
                ),
                Achievement(
                    id = "tower_builder",
                    title = "Tower Builder",
                    description = "Buy your first three upgrades."
                )
            )
        }
    }
}