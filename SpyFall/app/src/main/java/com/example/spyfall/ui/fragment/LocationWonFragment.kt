package com.example.spyfall.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.databinding.FragmentLocationWonBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationWonFragment :
    GameFragment<FragmentLocationWonBinding, ResultViewModel>(FragmentLocationWonBinding::inflate) {

    override val viewModel: ResultViewModel by viewModels()

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
                is ResultState.SetRole -> {
                    binding.locationImageView.setImageResource(state.role.drawable)
                }
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