package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import com.example.spyfall.ui.base.GameFragment
import javax.inject.Inject

class StartDirections @Inject constructor() {

    fun toPrepareWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_startFragment_to_prepareFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }

    fun toWaitingGameWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_startFragment_to_waitingGameFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }
}
