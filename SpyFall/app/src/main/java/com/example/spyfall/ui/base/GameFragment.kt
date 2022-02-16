package com.example.spyfall.ui.base

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.spyfall.R
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class GameFragment<VB : ViewBinding, VM : GameViewModel>(
    inflate: Inflate<VB>
) : DrawerFragment<VB, VM>(inflate) {

    protected val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.gameStateChannel.onEach { state ->
            when (state) {
                is GameState.ExitToMenu -> {
                    findNavController().navigateUp()
                }
                is GameState.ExitToLobbyForHost -> {
                    findNavController().navigate(R.id.prepareFragment, getBundle(gameId))
                }
                is GameState.ExitToLobbyForPlayer -> {
                    findNavController().navigate(R.id.waitingGameFragment, getBundle(gameId))
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.observeGameExit(gameId)
        viewModel.observeNumberOfPlayer(gameId)
    }

    companion object {
        private const val KEY_GAME_ID = "key_game_id"

        fun getBundle(gameId: String): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
            }
        }
    }
}