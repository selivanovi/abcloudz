package com.example.spyfall.ui.fragment.subfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.recyclerview.VotesAdapter
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class ListVoteFragment : Fragment(R.layout.fragment_list_vote) {

    private val parentViewModel: VoteViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val adapter = VotesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ListVoteView", "onViewCreated")

        // parentViewModel.playerChannel
        //     .onEach {
        //     Log.d("ListVoteView", "$it")
        //     adapter.setData(it)
        // }.launchIn(lifecycleScope)

        parentViewModel.playersChannel.receiveAsFlow()
            .onEach {
                Log.d("ListVoteView", "$it")
                adapter.setData(it)
            }.launchIn(lifecycleScope)

        createRecyclerView(view)
    }

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.voteRecyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}