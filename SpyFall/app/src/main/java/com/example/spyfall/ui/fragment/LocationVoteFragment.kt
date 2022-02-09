package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.VoteState
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationVoteFragment : BaseFragment<VoteViewModel>(R.layout.fragment_location_vote) {

    override val viewModel: VoteViewModel by viewModels()

    private val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<ListVoteFragment>(R.id.voteContainerView, args = ListVoteFragment.getBundle(gameId))
        }

        viewModel.voteStateChannel.onEach { state ->
            Log.d("VoteViewModel", "$state")
            when (state) {
                is VoteState.WaitOtherPlayers -> {
                    childFragmentManager.commit(allowStateLoss = true) {
                        setReorderingAllowed(true)
                        replace(R.id.voteContainerView, WaitingFragment())
                    }
                }
                is VoteState.SpyWon -> findNavController().navigate(
                    R.id.spyWonFragment,
                    SpyWonFragment.getBundle(gameId)
                )
                is VoteState.SpyLost -> findNavController().navigate(
                    R.id.locationWonFragment,
                    LocationWonFragment.getBundle(gameId)
                )
                is VoteState.GameContinue -> {
                    findNavController().navigate(R.id.roleFragment, RoleFragment.getBundle(gameId))
                }
            }

        }.launchIn(lifecycleScope)

        viewModel.gameStateChannel.onEach { state ->
            when (state) {
                is GameState.ExitToMenu -> {
                    findNavController().navigateUp()
                }
                is GameState.ExitToLobbyForHost -> {
                    findNavController().navigate(R.id.prepareFragment, PrepareFragment.getBundle(gameId))
                }
                is GameState.ExitToLobbyForPlayer -> {
                    findNavController().navigate(R.id.waitingGameFragment, PrepareFragment.getBundle(gameId))
                }
            }
        }.launchIn(lifecycleScope)


        viewModel.observeVotePlayersInGame(gameId)
        viewModel.observeGameExit(gameId)
        viewModel.observeNumberOfPlayer(gameId)
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