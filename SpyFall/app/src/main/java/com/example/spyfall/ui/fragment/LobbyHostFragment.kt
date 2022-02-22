package com.example.spyfall.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentLobbyHostBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.recyclerview.PlayersAdapter
import com.example.spyfall.ui.state.LobbyState
import com.example.spyfall.ui.viewmodel.LobbyViewModel
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.FragmentNotAttachedException
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LobbyHostFragment :
    BaseFragment<FragmentLobbyHostBinding, LobbyViewModel>(FragmentLobbyHostBinding::inflate) {

    override val viewModel: LobbyViewModel by viewModels()

    private val adapter = PlayersAdapter()
    private var lobbyFragmentListener: LobbyFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is LobbyFragmentListener) {
            lobbyFragmentListener = parent
        } else {
            throw FragmentNotAttachedException("LobbyHostFragment")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        view.findViewById<AppCompatButton>(R.id.buttonPlay).setOnClickListener {
            viewModel.setStatusPlayForPlayerInGame(gameId)
        }

        view.findViewById<MaterialButton>(R.id.inviteButton).setOnClickListener {
            sendApp(gameId)
        }

        viewModel.playersChannel.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.lobbyState.onEach { state ->
            if (state is LobbyState.Play) {
                lobbyFragmentListener?.startGame()
            }
        }.launchIn(lifecycleScope)

        createRecyclerView(view)

        viewModel.observePlayersFromGame(gameId)
    }

    private fun sendApp(gameId: String) {
        val packageName = requireContext().packageName

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

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.playersRecyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {

        private const val KEY_GAME_ID = "key_game_id"

        fun newInstance(gameId: String): LobbyHostFragment {
            val fragment = LobbyHostFragment().apply {
                arguments =  Bundle().apply {
                    putString(KEY_GAME_ID, gameId)
                }
            }
            return fragment
        }
    }
}
