package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentLobbyBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.recyclerview.PlayersAdapter
import com.example.spyfall.ui.state.LobbyState
import com.example.spyfall.ui.viewmodel.LobbyViewModel
import com.example.spyfall.utils.FragmentNotAttachedException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class LobbyFragment :
    BaseFragment<FragmentLobbyBinding, LobbyViewModel>(FragmentLobbyBinding::inflate) {

    override val viewModel: LobbyViewModel by viewModels()

    private val adapter = PlayersAdapter()
    private var lobbyFragmentListener: LobbyFragmentListener? = null

    private val gameId by lazy {
        requireArguments().getString(KEY_GAME_ID)
            ?: throw IllegalArgumentException("Game id is missing")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is LobbyFragmentListener) {
            lobbyFragmentListener = parent
        } else {
            throw FragmentNotAttachedException("Lobby fragment")
        }
    }

    override fun setupView() {
        super.setupView()
        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.playersRecyclerView.apply {
            adapter = adapter
            layoutManager = linearLayoutManager
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.buttonPlay.setOnClickListener {
            viewModel.setStatusPlayForPlayerInGame(gameId)
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.playersChannel.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.lobbyState.onEach { state ->
            if (state is LobbyState.Play) {
                lobbyFragmentListener?.startGame()
            }
        }.launchIn(lifecycleScope)

        viewModel.observePlayersFromGame(gameId)
    }

    override fun onDetach() {
        super.onDetach()
        lobbyFragmentListener = null
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
