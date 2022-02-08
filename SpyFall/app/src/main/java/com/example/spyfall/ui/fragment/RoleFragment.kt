package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.fragment.location.CallLocationFragment
import com.example.spyfall.ui.fragment.location.CheckLocationFragment
import com.example.spyfall.ui.fragment.vote.LocationVoteFragment
import com.example.spyfall.ui.fragment.vote.SpyVoteFragment
import com.example.spyfall.ui.state.RoleState
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

    private val gameId by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controlGameImageView = view.findViewById<AppCompatImageView>(R.id.controlGameImageView)
        val controlGameTextView = view.findViewById<TextView>(R.id.controlGameTextView)
        val timeTextView = view.findViewById<AppCompatTextView>(R.id.timeTextView)
        val locationButton = view.findViewById<AppCompatButton>(R.id.locationButton)
        val voteButton = view.findViewById<AppCompatButton>(R.id.voteButton)
        val controlGameLayout = view.findViewById<LinearLayout>(R.id.controlGameLayout)

        viewModel.roleChannel.onEach { role ->
            view.findViewById<AppCompatTextView>(R.id.locationTextView).setText(role.string)
            view.findViewById<AppCompatImageView>(R.id.locationImageView)
                .setImageResource(role.drawable)
        }.launchIn(lifecycleScope)


        viewModel.roleStateChannel.onEach { state ->
            when (state) {
                is RoleState.ExitToMenu -> {
                    findNavController().popBackStack(R.id.startFragment, false)
                }
                is RoleState.ExitToLobbyForHost -> {
                    findNavController().popBackStack(R.id.prepareFragment, false)
                }
                is RoleState.ExitTolLobbyForPlayer -> {
                    findNavController().popBackStack(R.id.waitingGameFragment, false)
                }
                is RoleState.GameIsPlaying -> {
                    controlGameImageView
                        .setImageResource(R.drawable.pause)
                    controlGameTextView
                        .setText(R.string.textPause)
                }
                is RoleState.GameIsPause -> {
                    controlGameImageView
                        .setImageResource(R.drawable.play)
                    view.findViewById<TextView>(R.id.controlGameTextView)
                        .setText(R.string.textContinue)
                }
                is RoleState.VoteSpy -> {
                    findNavController().navigate(
                        R.id.spyVoteFragment,
                        SpyVoteFragment.getBundle(gameId)
                    )
                }
                is RoleState.Player -> {
                    locationButton.isEnabled = false
                }
                is RoleState.Spy -> {
                    locationButton.isEnabled = true
                }
                is RoleState.VotePlayer -> {
                    findNavController().navigate(
                        R.id.locationVoteFragment,
                        LocationVoteFragment.getBundle(gameId)
                    )
                }
                is RoleState.LocationSpy -> {
                    findNavController().navigate(
                        R.id.callLocationFragment,
                        CallLocationFragment.getBundle(gameId)
                    )
                }
                is RoleState.LocationPlayer -> {
                    findNavController().navigate(
                        R.id.checkLocationFragment,
                        CheckLocationFragment.getBundle(gameId)
                    )
                }
                is RoleState.Voted -> {
                    voteButton.isEnabled = false
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.timeChannel.onEach {

            val minutes = it / 60
            val seconds = it % 60

            timeTextView.text = "$minutes:$seconds"
        }.launchIn(lifecycleScope)

        voteButton.setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.VOTE)
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.VOTED)
        }

        locationButton.setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.LOCATION)
        }

        controlGameLayout.setOnClickListener {
            viewModel.setPauseOrPlayForGame(gameId)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.setRolesInGame(gameId)
        viewModel.observeRoleOfCurrentPlayer(gameId)
        viewModel.observeGame(gameId)
        viewModel.observePlayersInGame(gameId)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTimer(gameId = gameId)
    }

    override fun onBackPressed() {
        viewModel.clearGame(gameId)
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
