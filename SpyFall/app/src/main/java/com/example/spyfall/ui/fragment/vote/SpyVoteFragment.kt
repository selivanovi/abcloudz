package com.example.spyfall.ui.fragment.vote

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.RoleFragment
import com.example.spyfall.ui.fragment.prepare.PrepareFragment
import com.example.spyfall.ui.fragment.prepare.WaitingGameFragment
import com.example.spyfall.ui.fragment.result.LocationWonFragment
import com.example.spyfall.ui.fragment.result.SpyWonFragment
import com.example.spyfall.ui.fragment.vote.sub.WaitingFragment
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.VoteState
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SpyVoteFragment : BaseFragment(R.layout.fragment_location_vote) {

    override val TAG: String
        get() = "SpyVoteFragment"

    private val viewModel: VoteViewModel by viewModels()

    private val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            add<WaitingFragment>(R.id.voteContainerView)
        }

        viewModel.voteStateChannel.onEach { state ->
            Log.d("VoteViewModel", "$state")
            when(state) {
                is VoteState.SpyWon -> findNavController().navigate(R.id.spyWonFragment, SpyWonFragment.getBundle(gameId))
                is VoteState.SpyLost -> findNavController().navigate(R.id.locationWonFragment, LocationWonFragment.getBundle(gameId))
                is VoteState.GameContinue -> findNavController().navigate(R.id.roleFragment,
                    RoleFragment.getBundle(gameId)
                )
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

    companion object {
        private const val KEY_GAME_ID = "key_game_id"

        fun getBundle(gameId: String): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
            }
        }
    }
}