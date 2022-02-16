package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.databinding.FragmentSpyWonBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SpyWonFragment :
    GameFragment<FragmentSpyWonBinding, ResultViewModel>(FragmentSpyWonBinding::inflate) {

    override val viewModel: ResultViewModel by viewModels()

    override fun setupView() {
        super.setupView()
        with(binding) {
            locationImageView.setImageResource(Role.SPY.drawable)
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            nextCardButton.setOnClickListener {
                viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.Continue)
            }
            mainMenuButton.setOnClickListener {
                viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.EXIT)
            }
        }
    }

    override fun setupObserver() {
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

        viewModel.observeGameExit(gameId)
        viewModel.observeStatusOfCurrentPlayer(gameId)
        viewModel.observeStatusOfPlayers(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }
}