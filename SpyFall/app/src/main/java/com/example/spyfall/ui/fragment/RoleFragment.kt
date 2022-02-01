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
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoleFragment : Fragment(R.layout.fragment_role) {

    private val viewModel: RoleViewModel by viewModels()

    private val gameId: String by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    private val user: User by lazy {
        requireArguments().getSerializable(KEY_USER_ID)!! as User
    }

    private val isHost: Boolean by lazy {
        requireArguments().getBoolean(KEY_IS_HOST)
    }

    private var isSpy: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationTextView = view.findViewById<AppCompatTextView>(R.id.locationTextView)
        val timeTextView = view.findViewById<AppCompatTextView>(R.id.timeTextView)
        val locationImageView = view.findViewById<AppCompatImageView>(R.id.locationImageView)
        val locationButton = view.findViewById<AppCompatButton>(R.id.locationButton)
        val voteButton = view.findViewById<AppCompatButton>(R.id.voteButton)

        viewModel.isHost = isHost

        viewModel.setRolesInGame(gameId = gameId)

        viewModel.playersChannel.onEach {
            it.forEach { player ->
                Log.d("RoleFragment", "User: ${user.userId} Player: ${player.playerId}")
                if (player.playerId == user.userId) {
                    player.role?.let { role ->
                        locationImageView.setImageResource(role.drawable)
                        locationTextView.text = resources.getString(role.string)
                        if (role != Role.SPY) {
                            locationButton.isEnabled = false
                        } else {
                            isSpy = true
                        }
                    }
                }
                if (player.status == PlayerStatus.VOTE) {
                    viewModel.pauseCountDownTimer()
                    findNavController().navigate(
                        R.id.voteFragment,
                        VoteFragment.getInstance(gameId, isSpy, false)
                    )
                }
                if (player.status == PlayerStatus.LOCATION) {

                }
            }
        }.launchIn(lifecycleScope)

        viewModel.timeChannel.onEach {
            if (it != null) {
                val minutes = it / 60
                val seconds = it % 60

                Log.d("RoleFragment", "$minutes:$seconds")

                timeTextView.text = "$minutes:$seconds"
            } else {

            }
        }.launchIn(lifecycleScope)

        voteButton.setOnClickListener {
            viewModel.setStatusForPLayerInGame(gameId, user.userId, PlayerStatus.VOTE)
        }

        locationButton.setOnClickListener {
            viewModel.setStatusForPLayerInGame(gameId, user.userId, PlayerStatus.LOCATION)
        }

        viewModel.observePLayersInGame(gameId)
        viewModel.startCountDownTimer(gameId)
    }

    companion object {
        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_IS_HOST = "key_is_host"

        fun getInstance(gameId: String, user: User, isHost: Boolean): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
                putSerializable(KEY_USER_ID, user)
                putBoolean(KEY_IS_HOST, isHost)
            }
        }
    }
}