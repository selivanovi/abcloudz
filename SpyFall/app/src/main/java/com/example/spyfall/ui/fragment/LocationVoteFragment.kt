package com.example.spyfall.ui.fragment

import android.view.View
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentLocationVoteBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.state.VoteState
import com.example.spyfall.ui.viewmodel.LocationVoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationVoteFragment :
    GameFragment<FragmentLocationVoteBinding, LocationVoteViewModel>(FragmentLocationVoteBinding::inflate) {

    override val viewModel: LocationVoteViewModel by viewModels()

    override fun setupView() {
        super.setupView()

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<ListVoteFragment>(R.id.voteContainerView, args = ListVoteFragment.getBundle(gameId))
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.voteStateChannel.onEach { state ->
            if (state is VoteState.WaitOtherPlayers) {
                childFragmentManager.commit(allowStateLoss = true) {
                    setReorderingAllowed(true)
                    replace(R.id.voteContainerView, WaitingFragment())
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.observeVotePlayersInGame(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }

    override fun setButtonDrawer(): View = binding.menu
}
