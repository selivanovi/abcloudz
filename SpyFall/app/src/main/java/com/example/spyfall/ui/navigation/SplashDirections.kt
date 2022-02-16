package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.SplashFragment
import javax.inject.Inject

class SplashDirections @Inject constructor() {

    fun toLogIn(): NavDirections = object : NavDirections {
        override val actionId: Int = R.id.action_splashFragment_to_logInFragment
        override val arguments: Bundle = Bundle()
    }
}