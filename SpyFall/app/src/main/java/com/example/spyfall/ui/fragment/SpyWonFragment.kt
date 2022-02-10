package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SpyWonFragment : BaseFragment<ResultViewModel>(R.layout.fragment_spy_won) {

    override val viewModel: ResultViewModel by viewModels()

    private val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        view.findViewById<AppCompatButton>(R.id.nextCardButton).setOnClickListener {
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.Continue)
        }
        view.findViewById<AppCompatButton>(R.id.mainMenuButton).setOnClickListener {
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.EXIT)
        }

        viewModel.observeGameExit(gameId)
        viewModel.observeStatusOfCurrentPlayer(gameId)
        viewModel.observeStatusOfPlayers(gameId)
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