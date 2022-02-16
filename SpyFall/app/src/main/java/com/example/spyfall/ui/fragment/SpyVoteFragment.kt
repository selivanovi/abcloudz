package com.example.spyfall.ui.fragment

import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentSpyVoteBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.viewmodel.SpyVoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SpyVoteFragment :
    GameFragment<FragmentSpyVoteBinding, SpyVoteViewModel>(FragmentSpyVoteBinding::inflate) {

    override val viewModel: SpyVoteViewModel by viewModels()

    override fun setupView() {
        super.setupView()

        childFragmentManager.commit {
            add<WaitingFragment>(R.id.voteContainerView)
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.observeVotePlayersInGame(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }
}