package com.example.spyfall.ui.base

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.spyfall.R
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import com.example.spyfall.utils.Inflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class ResultFragment<VB : ViewBinding>(
    inflate: Inflate<VB>
) : GameFragment<VB, ResultViewModel>(inflate) {

    override fun setupObserver() {
        super.setupObserver()
        viewModel.resultStateChannel.onEach { state ->
            when (state) {
                is ResultState.Exit ->
                    findNavController().popBackStack(
                        destinationId = R.id.startFragment,
                        inclusive = false
                    )
                is ResultState.HostContinue ->
                    findNavController().popBackStack(
                        destinationId = R.id.prepareFragment,
                        inclusive = false
                    )
                is ResultState.PlayerContinue ->
                    findNavController().popBackStack(
                        destinationId = R.id.waitingGameFragment,
                        inclusive = false
                    )
            }
        }.launchIn(lifecycleScope)

        viewModel.gameStateChannel.onEach { state ->
            if (state is GameState.ExitToMenu) {
                findNavController().popBackStack(R.id.startFragment, false)
            }
        }.launchIn(lifecycleScope)

        viewModel.observeStatusOfCurrentPlayer(gameId)
        viewModel.observeStatusOfPlayers(gameId)
    }

    override fun handleGameExit(state: GameState) {
        viewModel.navigateUp()
    }
}