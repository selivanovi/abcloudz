package com.example.spyfall.ui.viewmodel

import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.navigation.SplashDirections
import com.example.spyfall.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel() {

    fun navigateToLogIn() {
        launch {
            delay(Constants.SPLASH_DELAY)
            navigateTo(SplashDirections.toLogIn())
        }
    }
}
