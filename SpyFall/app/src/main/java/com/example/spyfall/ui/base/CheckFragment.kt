package com.example.spyfall.ui.base

import androidx.viewbinding.ViewBinding
import com.example.spyfall.utils.Inflate

abstract class CheckFragment<VB : ViewBinding, VM : CheckResultViewModel>(
    inflate: Inflate<VB>
) : GameFragment<VB, CheckResultViewModel>(inflate) {

    override fun setupObserver() {
        super.setupObserver()

        viewModel.observeGameStatusForResult(gameId)
    }
}