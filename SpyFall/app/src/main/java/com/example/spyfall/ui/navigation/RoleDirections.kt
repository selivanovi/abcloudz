package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.fragment.CallLocationFragment
import com.example.spyfall.ui.fragment.CheckLocationFragment
import com.example.spyfall.ui.fragment.LocationVoteFragment
import com.example.spyfall.ui.fragment.SpyVoteFragment
import javax.inject.Inject

class RoleDirections @Inject constructor() {

    fun toLocationVoteWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_roleFragment_to_locationVoteFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }

    fun toSpyVoteWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_roleFragment_to_spyVoteFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }


    fun toCheckLocationWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_roleFragment_to_checkLocationFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }


    fun toCallLocationWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_roleFragment_to_callLocationFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }
}