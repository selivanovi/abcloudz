package com.example.spyfall.ui.screen.view

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
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.ui.screen.listener.StartGameListener
import com.example.spyfall.ui.screen.recyclerview.PlayersAdapter
import com.example.spyfall.ui.viewmodel.CreateGameViewModel
import com.example.spyfall.ui.viewmodel.PlayersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class InvitePlayerView : Fragment(R.layout.fragment_invite_player_view) {

    private val parentViewModel: CreateGameViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })
    private val viewModel: PlayersViewModel by viewModels()

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
                val gameId = parentViewModel.gameId
                val user = parentViewModel.user
                if (gameId != null && user != null) {
                    viewModel.setPlayerPlayInGame(gameId, user.userId, user.name)
                }
            }
        }

        parentViewModel.gameId?.let {
            viewModel.observePlayersFromGame(it)
        }

        viewModel.playersChannel.onEach {
            if (it.all { player -> player.status == PlayerStatus.PLAY }) {
                startGameListener?.startGame()
            }
            if (it.size >= Constants.MIN_NUMBER_PLAYERS) {
                buttonPlay.isEnabled = true
            }
            adapter.setData(it)
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