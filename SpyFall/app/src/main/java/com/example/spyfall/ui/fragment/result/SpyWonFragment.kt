package com.example.spyfall.ui.fragment.result

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.prepare.WaitingGameFragment
import com.example.spyfall.ui.fragment.vote.SpyVoteFragment
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SpyWonFragment : BaseFragment(R.layout.fragment_spy_won) {

    override val TAG: String
        get() = "SpyWonFragment"

    private val viewModel: ResultViewModel by viewModels()

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

        view.findViewById<AppCompatButton>(R.id.nextCardButton).setOnClickListener {
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.Continue)
        }
        view.findViewById<AppCompatButton>(R.id.mainMenuButton).setOnClickListener {
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.EXIT)
        }

        viewModel.observeStatusOfCurrentPlayer(gameId)
        viewModel.observeStatusOfPlayers(gameId)
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