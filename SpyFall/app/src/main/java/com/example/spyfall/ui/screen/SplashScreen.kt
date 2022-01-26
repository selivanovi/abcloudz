package com.example.spyfall.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.viewmodel.SetNameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.fragment_splash) {

    private val viewModel: SetNameViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            delay(1000)
            if (viewModel.getUser() != null) {
                findNavController().navigate(R.id.action_splashScreen_to_startGameScreen)
            }else {
                findNavController().navigate(R.id.action_splashScreen_to_enterNameScreen)
            }
        }
    }
}