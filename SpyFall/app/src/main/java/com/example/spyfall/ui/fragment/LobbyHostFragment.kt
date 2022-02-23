package com.example.spyfall.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentLobbyHostBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.recyclerview.PlayersAdapter
import com.example.spyfall.ui.state.LobbyState
import com.example.spyfall.ui.viewmodel.LobbyViewModel
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.FragmentNotAttachedException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LobbyHostFragment :
    BaseFragment<FragmentLobbyHostBinding, LobbyViewModel>(FragmentLobbyHostBinding::inflate) {

    override val viewModel: LobbyViewModel by viewModels()

    private val adapter = PlayersAdapter()
    private var lobbyFragmentListener: LobbyFragmentListener? = null

    val gameId by lazy {
        requireArguments().getString(KEY_GAME_ID)
            ?: throw IllegalArgumentException("Game id is not found")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is LobbyFragmentListener) {
            lobbyFragmentListener = parent
        } else {
            throw FragmentNotAttachedException("LobbyHostFragment")
        }
    }

    override fun setupView() {
        super.setupView()
        with(binding) {
            playersRecyclerView.adapter = adapter
            playersRecyclerView.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            buttonPlay.setOnClickListener {
                viewModel.setStatusPlayForPlayerInGame(gameId)
            }

            inviteButton.setOnClickListener {
                sendApp(gameId)
            }
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

    private fun sendApp(gameId: String) {

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = Constants.TEXT_TYPE
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.invite) +
                        "\n" +
                        Constants.APP_URL +
                        "?id=$gameId"
            )
            type = Constants.IMAGE_TYPE
            putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse(Constants.IMAGE_URI)
            )
        }

        try {
            startActivity(Intent.createChooser(intent, resources.getString(R.string.share)))
        } catch (e: Exception) {
            Log.e("LobbyHostFragment", e.message.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()

        lobbyFragmentListener = null
    }

    companion object {

        private const val KEY_GAME_ID = "key_game_id"

        fun newInstance(gameId: String): LobbyHostFragment {
            val fragment = LobbyHostFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_GAME_ID, gameId)
                }
            }
            return fragment
        }
    }
}
