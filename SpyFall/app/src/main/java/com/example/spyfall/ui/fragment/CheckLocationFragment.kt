package com.example.spyfall.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.databinding.FragmentCheckLocationBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.viewmodel.CheckLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckLocationFragment :
    GameFragment<FragmentCheckLocationBinding, CheckLocationViewModel>(FragmentCheckLocationBinding::inflate) {

    override val viewModel: CheckLocationViewModel by viewModels()

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            buttonCorrectly.setOnClickListener {
                viewModel.setStatusForGame(gameId, GameStatus.SPY_WON)
            }
            buttonCorrectly.setOnClickListener {
                viewModel.setStatusForGame(gameId, GameStatus.LOCATION_WON)
            }
        }
    }

    override fun setupObserver() {
        super.setupObserver()
        viewModel.observeGameStatusForResult(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }
}
