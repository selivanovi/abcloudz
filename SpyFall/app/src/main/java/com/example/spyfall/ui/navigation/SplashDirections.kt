package com.example.spyfall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.spyfall.R
import javax.inject.Inject

object SplashDirections {

    fun toLogIn(): NavDirections = object : NavDirections {
        override val actionId: Int = R.id.action_splashFragment_to_logInFragment
        override val arguments: Bundle = Bundle()
    }
}
