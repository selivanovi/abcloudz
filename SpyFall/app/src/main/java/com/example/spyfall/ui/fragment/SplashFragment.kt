package com.example.spyfall.ui.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.example.spyfall.databinding.FragmentSplashBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashFragment")
@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(FragmentSplashBinding::inflate) {

    override val viewModel: SplashViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        viewModel.navigateToLogIn()
    }
}
