package com.example.spyfall.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHost
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.screen.customview.OutlineTextView
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(R.layout.fragment_splash) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            delay(1000)
            findNavController().navigate(R.id.action_splashScreen_to_enterNameScreen)
        }
    }
}