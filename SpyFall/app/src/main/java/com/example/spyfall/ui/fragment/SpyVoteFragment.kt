package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.subfragment.ListVoteFragment
import com.example.spyfall.ui.fragment.subfragment.WaitingGameFragment
import com.example.spyfall.ui.viewmodel.VoteState
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SpyVoteFragment : Fragment(R.layout.fragment_location_vote) {

    private val viewModel: VoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        childFragmentManager.commit {
            add<WaitingGameFragment>(R.id.voteContainerView)
        }

        viewModel.voteStateChannel.onEach { state ->
            Log.d("VoteViewModel", "$state")
            when(state) {
                is VoteState.SpyWonState -> findNavController().navigate(R.id.spyWonFragment)
                is VoteState.SpyLostState -> findNavController().navigate(R.id.locationWonFragment)
                is VoteState.GameContinueState -> findNavController().popBackStack()
            }

        }.launchIn(lifecycleScope)


        viewModel.observeVotePlayersInGame(gameId)
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