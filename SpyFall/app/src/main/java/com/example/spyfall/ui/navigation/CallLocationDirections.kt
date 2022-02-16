package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import com.example.spyfall.ui.base.GameFragment
import javax.inject.Inject

class CallLocationDirections @Inject constructor(){

    fun toLocationWonWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_callLocationFragment_to_locationWonFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }

    fun toSpyWonWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_callLocationFragment_to_spyWonFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }
}