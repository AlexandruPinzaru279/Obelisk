package com.example.idletest.domain.actions

// NOT IMPLEMENTED / USED
sealed class GameAction {
    data object StartWave : GameAction()
    data class BuyUpgrade(val upgradeId: String) : GameAction()
    data class UseAbility(val abilityId: String) : GameAction()
    data object ClaimWaveReward : GameAction()
}