package com.example.spyfall.ui.fragment.subfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.listener.StartGameListener
import com.example.spyfall.ui.fragment.recyclerview.PlayersAdapter
import com.example.spyfall.ui.viewmodel.CreateGameViewModel
import com.example.spyfall.ui.viewmodel.LobbyViewModel
import com.example.spyfall.ui.viewmodel.PlayState
import com.example.spyfall.ui.viewmodel.WaitState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LobbyFragment : Fragment(R.layout.fragment_invite_player_view) {

    private val parentViewModel: CreateGameViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })
    private val viewModel: LobbyViewModel by viewModels()

    private val adapter = PlayersAdapter()
    private var startGameListener: StartGameListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment().requireParentFragment()
        if (parent is StartGameListener) {
            startGameListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonPlay = view.findViewById<AppCompatButton>(R.id.buttonPlay).apply {
            isEnabled = false
            setOnClickListener {
                viewModel.setStatusPlayForPlayerInGame()
            }
        }

        viewModel.observePlayersFromGame()

        viewModel.playersChannel.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.lobbyState.onEach { state ->
           when(state) {
               is PlayState -> {
                   startGameListener?.startGame()
                   buttonPlay.isEnabled = true
               }
               is WaitState -> buttonPlay.isEnabled = false
           }
        }.launchIn(lifecycleScope)

        createRecyclerView(view)
    }

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.playersRecyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}