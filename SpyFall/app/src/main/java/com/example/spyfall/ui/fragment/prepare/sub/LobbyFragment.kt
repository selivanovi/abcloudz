package com.example.spyfall.ui.fragment.prepare.sub

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
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.fragment.prepare.WaitingGameFragment
import com.example.spyfall.ui.listener.StartGameListener
import com.example.spyfall.ui.recyclerview.PlayersAdapter
import com.example.spyfall.ui.viewmodel.CreateGameViewModel
import com.example.spyfall.ui.viewmodel.LobbyViewModel
import com.example.spyfall.ui.viewmodel.PlayState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LobbyFragment : Fragment(R.layout.fragment_lobby) {

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

        val user: User = requireArguments().getSerializable(KEY_USER)!! as User
        val gameId: String = requireArguments().getString(KEY_GAME_ID)!!

        view.findViewById<AppCompatButton>(R.id.buttonPlay).apply {
            setOnClickListener {
                viewModel.setStatusPlayForPlayerInGame(gameId, user.userId)
            }
        }

        viewModel.observePlayersFromGame(gameId)

        viewModel.playersChannel.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.lobbyState.onEach { state ->
            when(state) {
                is PlayState -> {
                    startGameListener?.startGame()
                }
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

    companion object {

        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_USER = "key_user"

        fun getBundle(user: User, gameId: String): Bundle {
            return Bundle().apply {
                putSerializable(KEY_USER, user)
                putString(KEY_GAME_ID, gameId)
            }
        }
    }
}