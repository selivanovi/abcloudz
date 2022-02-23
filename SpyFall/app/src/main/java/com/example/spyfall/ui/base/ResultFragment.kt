package com.example.spyfall.ui.base

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.spyfall.R
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.utils.Inflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class ResultFragment<VB : ViewBinding, VM : CheckResultViewModel>(
    inflate: Inflate<VB>
) : GameFragment<VB, CheckResultViewModel>(inflate) {

    override fun setupObserver() {
        viewModel.gameStateChannel.onEach { state ->
            when (state) {
                GameState.ExitToMenu -> findNavController().navigateUp()
                GameState.ExitToLobbyForHost ->
                    findNavController().navigate(R.id.prepareFragment, getBundle(gameId))
                GameState.ExitToLobbyForPlayer ->
                    findNavController().navigate(R.id.waitingGameFragment, getBundle(gameId))
            }
        }.launchIn(lifecycleScope)

        viewModel.observeGameStatusForResult(gameId)
        viewModel.observeGameExit(gameId)
    }
}