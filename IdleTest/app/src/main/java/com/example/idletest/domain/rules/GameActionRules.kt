package com.example.idletest.domain.rules

import com.example.idletest.domain.actions.GameAction
import com.example.idletest.domain.model.GameState

// NOT IMPLEMENTED/ USED
fun GameState.applyAction(action: GameAction) : GameState {
    return when(action) {
        is GameAction.StartWave -> startWave()

        is GameAction.BuyUpgrade -> buyUpgrade(action.upgradeId)
        // not implemented yet
        is GameAction.UseAbility -> this
        //not implemented yet
        is GameAction.ClaimWaveReward -> this
    }
}