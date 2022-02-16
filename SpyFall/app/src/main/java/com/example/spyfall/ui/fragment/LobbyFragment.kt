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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LobbyFragment : BaseFragment<FragmentLobbyBinding, LobbyViewModel>(FragmentLobbyBinding::inflate) {

    override val viewModel: LobbyViewModel by viewModels()

    private val adapter = PlayersAdapter()
    private var lobbyFragmentListener: LobbyFragmentListener? = null

    private val gameId by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is LobbyFragmentListener) {
            lobbyFragmentListener = parent
        }
    }

    override fun setupView() {
        super.setupView()
        createRecyclerView(requireView())
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

    override fun onStop() {
        super.onStop()
        lifecycleScope.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        lobbyFragmentListener = null
    }

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.playersRecyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
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
