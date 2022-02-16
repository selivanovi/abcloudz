package com.example.spyfall.ui.viewmodel

import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.navigation.SplashDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fragmentDirections: SplashDirections
) : BaseViewModel() {

    fun navigateToLogIn() {
        launch {
            delay(1000)
            navigateTo(fragmentDirections.toLogIn())
        }
    }
}