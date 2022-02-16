package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import com.example.spyfall.ui.base.GameFragment
import javax.inject.Inject

class PrepareGameDirections @Inject constructor() {

    fun toRoleWithArgs(gameId: String) = object : NavDirections {
        override val actionId: Int = R.id.action_prepareFragment_to_roleFragment
        override val arguments: Bundle = GameFragment.getBundle(gameId)
    }
}
