package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.viewmodel.RoleState
import com.example.spyfall.ui.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoleFragment : Fragment(R.layout.fragment_role) {

    private val viewModel: RoleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!
        val isHost = requireArguments().getBoolean(KEY_IS_HOST)

        val locationTextView = view.findViewById<AppCompatTextView>(R.id.locationTextView)
        val timeTextView = view.findViewById<AppCompatTextView>(R.id.timeTextView)
        val locationImageView = view.findViewById<AppCompatImageView>(R.id.locationImageView)
        val locationButton = view.findViewById<AppCompatButton>(R.id.locationButton)
        val voteButton = view.findViewById<AppCompatButton>(R.id.voteButton)

        viewModel.observeRoleOfCurrentPlayer(gameId)
        viewModel.observeGame(gameId)


        Log.d("RoleFragment", "set role fragment")

        viewModel.roleStateChannel.onEach { state ->
            when (state) {
                is RoleState.SetRoleState -> {
                    locationTextView.text = resources.getString(state.role.string)
                    locationImageView.setImageResource(state.role.drawable)
                }
                is RoleState.VoteSpyState -> {
                    findNavController().navigate(R.id.spyVoteFragment, SpyVoteFragment.getBundle(gameId))
                }
                is RoleState.PlayerState -> {
                    locationButton.isEnabled = false
                }
                is RoleState.SpyState -> {
                    locationButton.isEnabled = true
                }
                is RoleState.VotePlayerState -> {
                    findNavController().navigate(R.id.locationVoteFragment, LocationVoteFragment.getBundle(gameId))
                }
            }
        }.launchIn(lifecycleScope)

        voteButton.setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.VOTE)
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.VOTED)
        }

        locationButton.setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.LOCATION)
        }

        viewModel.setRolesInGame(gameId, isHost)
    }

    companion object {


        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_IS_HOST = "key_is_host"

        fun getBundle(gameId: String, isHost: Boolean): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
                putBoolean(KEY_IS_HOST, isHost)
            }
        }
    }
}
