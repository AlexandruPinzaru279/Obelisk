package com.example.idletest.domain.model

data class Ability(
    val id: String,
    val name: String,
    val level: Int,
    val cooldown: Double,
    val cooldownRemaining: Double
) {
    companion object {
        fun defaultAbilities(): List<Ability> {
            return listOf(
                Ability(
                    id = "push",
                    name = "Force push",
                    level = 1,
                    cooldown = 20.0,
                    cooldownRemaining = 0.0
                ),

                Ability(
                    id = "shockwave",
                    name = "Shockwave",
                    level = 1,
                    cooldown = 15.0,
                    cooldownRemaining = 0.0
                ),

                Ability(
                    id = "repair",
                    name = "Repair",
                    level = 1,
                    cooldown = 30.0,
                    cooldownRemaining = 0.0
                )
            )
        }
    }
}
