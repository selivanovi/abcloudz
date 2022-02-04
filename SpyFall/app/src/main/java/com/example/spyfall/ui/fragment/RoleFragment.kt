package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.fragment.location.CallLocationFragment
import com.example.spyfall.ui.fragment.location.CheckLocationFragment
import com.example.spyfall.ui.fragment.vote.LocationVoteFragment
import com.example.spyfall.ui.fragment.vote.SpyVoteFragment
import com.example.spyfall.ui.viewmodel.RoleState
import com.example.spyfall.ui.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoleFragment : BaseFragment(R.layout.fragment_role) {

    override val TAG: String
        get() = "RoleFragment"

    private val viewModel: RoleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        val locationTextView = view.findViewById<AppCompatTextView>(R.id.locationTextView)
        val timeTextView = view.findViewById<AppCompatTextView>(R.id.timeTextView)
        val locationImageView = view.findViewById<AppCompatImageView>(R.id.locationImageView)
        val locationButton = view.findViewById<AppCompatButton>(R.id.locationButton)
        val voteButton = view.findViewById<AppCompatButton>(R.id.voteButton)

        viewModel.observeRoleOfCurrentPlayer(gameId)
        viewModel.observeGame(gameId)
        viewModel.setStatusForGame(gameId, GameStatus.PLAYING)

        viewModel.roleChannel.onEach { role ->
            locationTextView.setText(role.string)
            locationImageView.setImageResource(role.drawable)
        }.launchIn(lifecycleScope)


        viewModel.roleStateChannel.onEach { state ->
            when (state) {
                is RoleState.VoteSpyState -> {
                    findNavController().navigate(
                        R.id.spyVoteFragment,
                        SpyVoteFragment.getBundle(gameId)
                    )
                }
                is RoleState.PlayerState -> {
                    locationButton.isEnabled = false
                }
                is RoleState.SpyState -> {
                    locationButton.isEnabled = true
                }
                is RoleState.VotePlayerState -> {
                    findNavController().navigate(
                        R.id.locationVoteFragment,
                        LocationVoteFragment.getBundle(gameId)
                    )
                }
                is RoleState.LocationSpyState -> {
                    findNavController().navigate(
                        R.id.callLocationFragment,
                        CallLocationFragment.getBundle(gameId)
                    )
                }
                is RoleState.LocationPlayerState -> {
                    findNavController().navigate(
                        R.id.checkLocationFragment,
                        CheckLocationFragment.getBundle(gameId)
                    )
                }
                is RoleState.VotedState -> {
                    voteButton.isEnabled = false
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

        viewModel.setRolesInGame(gameId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
