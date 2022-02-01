package com.example.spyfall.ui.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashFragment")
@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {


    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            delay(1000)
            findNavController().navigate(R.id.action_splashFragment_to_enterNameFragment)
        }
    }
}