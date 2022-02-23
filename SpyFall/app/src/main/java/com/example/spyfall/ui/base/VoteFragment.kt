package com.example.spyfall.ui.base

import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.spyfall.ui.state.VoteState
import com.example.spyfall.utils.Inflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class VoteFragment<VB : ViewBinding, VM : VoteViewModel>(
    inflate: Inflate<VB>
) : GameFragment<VB, VM>(inflate) {

    override fun setupObserver() {
        super.setupObserver()

        viewModel.voteStateChannel.onEach { state ->
            handleState(state)
        }.launchIn(lifecycleScope)

        viewModel.observeVotePlayersInGame(gameId)
    }

    open fun handleState(state: VoteState) {
    }
}