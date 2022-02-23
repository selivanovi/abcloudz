package com.example.spyfall.ui.fragment

import android.view.View
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentLocationVoteBinding
import com.example.spyfall.ui.base.VoteFragment
import com.example.spyfall.ui.state.VoteState
import com.example.spyfall.ui.viewmodel.LocationVoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationVoteFragment :
    VoteFragment<FragmentLocationVoteBinding, LocationVoteViewModel>(FragmentLocationVoteBinding::inflate) {

    override val viewModel: LocationVoteViewModel by viewModels()

    override fun setupView() {
        super.setupView()
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<ListVoteFragment>(R.id.voteContainerView, args = ListVoteFragment.getBundle(gameId))
        }
    }

    override fun handleState(state: VoteState) {
        if (state is VoteState.WaitOtherPlayers) {
            childFragmentManager.commit(allowStateLoss = true) {
                setReorderingAllowed(true)
                replace(R.id.voteContainerView, WaitingFragment())
            }
        }
    }

    override fun setButtonDrawer(): View = binding.menu
}
