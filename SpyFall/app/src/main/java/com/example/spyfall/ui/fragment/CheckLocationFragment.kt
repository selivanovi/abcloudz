package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.ui.state.CheckState
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.viewmodel.CheckLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckLocationFragment :
    BaseFragment<CheckLocationViewModel>(R.layout.fragment_check_location) {


    override val viewModel: CheckLocationViewModel by viewModels()

    val gameId by lazy { requireArguments().getString(KEY_GAME_ID)!! }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkStateChannel.onEach { state ->
            if (state == CheckState.SpyLost) {
                findNavController().navigate(
                    R.id.locationWonFragment,
                    LocationWonFragment.getBundle(gameId)
                )
            } else {
                findNavController().navigate(R.id.spyWonFragment, SpyWonFragment.getBundle(gameId))
            }
        }.launchIn(lifecycleScope)

        view.findViewById<AppCompatButton>(R.id.buttonCorrectly).setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.SPY_WON)
        }
        view.findViewById<AppCompatButton>(R.id.buttonCorrectly).setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.LOCATION_WON)
        }

        viewModel.gameStateChannel.onEach { state ->
            when (state) {
                is GameState.ExitToMenu -> {
                    findNavController().navigateUp()
                }
                is GameState.ExitToLobbyForHost -> {
                    findNavController().navigate(
                        R.id.prepareFragment,
                        PrepareFragment.getBundle(gameId)
                    )
                }
                is GameState.ExitToLobbyForPlayer -> {
                    findNavController().navigate(
                        R.id.waitingGameFragment,
                        PrepareFragment.getBundle(gameId)
                    )
                }
            }
        }.launchIn(lifecycleScope)


        viewModel.observeGameExit(gameId)
        viewModel.observeNumberOfPlayer(gameId)
        viewModel.observeGame(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
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