package com.example.spyfall.ui.fragment.subfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.ui.fragment.recyclerview.VotesAdapter
import com.example.spyfall.ui.viewmodel.ListVoteViewModel
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class ListVoteFragment : Fragment(R.layout.fragment_list_vote) {

    private val viewModel: ListVoteViewModel by viewModels()

    private val adapter = VotesAdapter(::changeItem)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        viewModel.playersChannel.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)


        view.findViewById<AppCompatButton>(R.id.voteButton).setOnClickListener {
            viewModel.sendVoteForCurrentPLayerInGame(gameId)
        }
        createRecyclerView(view)
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