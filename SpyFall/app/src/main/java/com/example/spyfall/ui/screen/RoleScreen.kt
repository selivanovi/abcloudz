package com.example.spyfall.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoleScreen : Fragment(R.layout.fragment_role) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationTextView = view.findViewById<AppCompatTextView>(R.id.locationTextView)
        val locationImageView = view.findViewById<AppCompatImageView>(R.id.locationImageView)
        val locationButton = view.findViewById<AppCompatButton>(R.id.locationButton)
        val voteButton = view.findViewById<AppCompatButton>(R.id.voteButton)

        viewModel.isHost = isHost

        viewModel.setRolesInGame(gameId = gameId)

        viewModel.playersChannel.onEach {
            it.forEach { player ->
                Log.d("RoleScreen", "User: ${user.userId} Player: ${player.playerId}")
                if(player.playerId == user.userId){
                    player.role?.let{ role ->
                        locationImageView.setImageResource(role.drawable)
                        locationTextView.text = resources.getString(role.string)
                        if(role != Role.SPY){
                            locationButton.isEnabled = false
                        }
                    }
                }
            }
        }.launchIn(lifecycleScope)

        locationButton.setOnClickListener {
        }

        viewModel.observePLayersInGame(gameId)
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