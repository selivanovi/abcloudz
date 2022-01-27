package com.example.spyfall.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
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

    private val isHost: Boolean by lazy {
        requireArguments().getBoolean(KEY_IS_HOST)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationTextView = view.findViewById<AppCompatTextView>(R.id.locationTextView)
        val locationImageView = view.findViewById<AppCompatImageView>(R.id.locationImageView)

        viewModel.isHost = isHost

        viewModel.setRolesForPlayersInGame(gameId = gameId)

        viewModel.roleChannel.onEach {
            locationImageView.setImageResource(it.drawable)
            locationTextView.text = resources.getString(it.string)
        }.launchIn(lifecycleScope)

        view.findViewById<Button>(R.id.locationButton).setOnClickListener {
        }
    }

    companion object {
        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_IS_HOST = "key_is_host"

        fun getInstance(gameId: String, isHost: Boolean): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
                putBoolean(KEY_IS_HOST, isHost)
            }
        }
    }
}