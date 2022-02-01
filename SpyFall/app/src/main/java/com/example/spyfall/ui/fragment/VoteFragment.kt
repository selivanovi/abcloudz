package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.subfragment.WaitingGameFragment
import com.example.spyfall.ui.viewmodel.VoteState
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class VoteFragment : Fragment(R.layout.fragment_vote) {

    private val viewModel: VoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.voteStateChannel.onEach { state ->
            when(state) {
                is VoteState.WaitOtherPlayersState -> {
                    childFragmentManager.commit {
                        replace<WaitingGameFragment>(R.id.voteContainerView)
                    }
                }
                is VoteState.SpyWonState -> findNavController().navigate(R.id.resultFragment)
                is VoteState.SpyLostState -> findNavController().navigate(R.id.resultFragment)
                is VoteState.GameContinueState -> findNavController().popBackStack()
            }

        }.launchIn(lifecycleScope)


        viewModel.observeVotePlayersInGameNew()
    }
}