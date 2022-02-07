package com.example.spyfall.ui.fragment.prepare.sub

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.recyclerview.PlayersAdapter
import com.example.spyfall.ui.state.LobbyState
import com.example.spyfall.ui.viewmodel.LobbyViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class LobbyHostFragment : Fragment(R.layout.fragment_lobby_host) {

    private val viewModel: LobbyViewModel by viewModels()

    private val adapter = PlayersAdapter()
    private var lobbyFragmentListener: LobbyFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("LobbyFragment", "onAttach")
        val parent = requireParentFragment()
        if (parent is LobbyFragmentListener) {
            lobbyFragmentListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LobbyFragment", "onViewCreated")

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        view.findViewById<AppCompatButton>(R.id.buttonPlay).setOnClickListener {
            viewModel.setStatusPlayForPlayerInGame(gameId)
        }


        view.findViewById<MaterialButton>(R.id.inviteButton).setOnClickListener {
            sendApp(gameId)
        }

        viewModel.observePlayersFromGame(gameId)

        viewModel.playersChannel.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.lobbyState.onEach { state ->
            if (state is LobbyState.Play) {
                lobbyFragmentListener?.startGame()
            }
        }.launchIn(lifecycleScope)

        createRecyclerView(view)
    }

    private fun sendApp(gameId: String) {
        try {
            val packageName = requireContext().packageName
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "My app name")
            var strShareMessage = resources.getString(R.string.invite)+"\n"
            strShareMessage += "https://spyfall.example.com?id=$gameId"
            val screenshotUri = Uri.parse("android.resource://$packageName/drawable/image_spy")
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            intent.putExtra(Intent.EXTRA_TEXT, strShareMessage)
            startActivity(Intent.createChooser(intent, resources.getString(R.string.share)))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LobbyFragment", "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LobbyFragment", "onStop")
        lifecycleScope.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LobbyFragment", "onDetach")
        lobbyFragmentListener = null
    }

    private fun createRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.playersRecyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {

        private const val TAG = "InvitePLayerFragment"

        private const val KEY_GAME_ID = "key_game_id"

        fun newInstance(gameId: String): LobbyHostFragment {
            val bundle = Bundle().apply {
                putString(KEY_GAME_ID, gameId)
            }
            val fragment = LobbyHostFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }

}