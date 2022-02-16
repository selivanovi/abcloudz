package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentListVoteBinding
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.recyclerview.VotesAdapter
import com.example.spyfall.ui.viewmodel.ListVoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ListVoteFragment : BaseFragment<FragmentListVoteBinding, ListVoteViewModel>(FragmentListVoteBinding::inflate) {

    override val viewModel: ListVoteViewModel by viewModels()

    private val adapter = VotesAdapter(::changeItem)

    private val gameId by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    override fun setupView() {
        super.setupView()
        createRecyclerView(requireView())
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.voteButton.setOnClickListener {
            viewModel.sendVoteForCurrentPLayerInGame(gameId)
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.playersChannel.onEach { players ->
            adapter.setData(players)
        }.launchIn(lifecycleScope)

        viewModel.observePlayersFromGame(gameId)
    }

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.voteRecyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    private fun changeItem(playerDomain: PlayerDomain) {
        viewModel.playerForVote = playerDomain
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
