package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import javax.inject.Inject

class LogInDirections @Inject constructor() {

    fun toStart(): NavDirections = object : NavDirections {

        override val actionId: Int = R.id.action_logInFragment_to_startFragment
        override val arguments: Bundle = Bundle()
    }
}