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
import com.example.spyfall.ui.fragment.result.LocationWonFragment
import com.example.spyfall.ui.fragment.result.SpyWonFragment
import com.example.spyfall.ui.fragment.vote.sub.ListVoteFragment
import com.example.spyfall.ui.fragment.vote.sub.WaitingFragment
import com.example.spyfall.ui.viewmodel.VoteState
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LocationVoteFragment : BaseFragment(R.layout.fragment_location_vote) {

    override val TAG: String
        get() = "LocationVoteFragment"

    private val viewModel: VoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        childFragmentManager.commit {
            add<ListVoteFragment>(R.id.voteContainerView, args = ListVoteFragment.getBundle(gameId))
        }

        viewModel.voteStateChannel.onEach { state ->
            Log.d("VoteViewModel", "$state")
            when(state) {
                is VoteState.WaitOtherPlayersState -> {
                    childFragmentManager.commit(allowStateLoss = true) {
                        setReorderingAllowed(true)
                        replace(R.id.voteContainerView, WaitingFragment())
                    }
                }
                is VoteState.SpyWonState -> findNavController().navigate(R.id.spyWonFragment, SpyWonFragment.getBundle(gameId))
                is VoteState.SpyLostState -> findNavController().navigate(R.id.locationWonFragment, LocationWonFragment.getBundle(gameId))
                is VoteState.GameContinueState -> {
                    findNavController().navigate(R.id.roleFragment, RoleFragment.getBundle(gameId))
                }
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