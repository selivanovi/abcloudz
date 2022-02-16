package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
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
import com.example.spyfall.databinding.FragmentRoleBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.RoleState
import com.example.spyfall.ui.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoleFragment :
    GameFragment<FragmentRoleBinding, RoleViewModel>(FragmentRoleBinding::inflate) {

    override val viewModel: RoleViewModel by viewModels()

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
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
    }

    override fun setupObserver() {
        super.setupObserver()
        with(binding) {
            viewModel.roleStateChannel.onEach { state ->
                when (state) {
                    is RoleState.SetRole -> {
                        locationTextView.setText(state.role.string)
                        locationImageView.setImageResource(state.role.drawable)
                    }
                    is RoleState.SetTime -> {
                        val minutes = state.time / 60
                        val seconds = state.time % 60

                        binding.timeTextView.text =
                            resources.getString(R.string.time, minutes, seconds)
                    }
                    is RoleState.GameIsPlaying -> {
                        controlGameImageView.setImageResource(R.drawable.pause)
                        controlGameTextView.setText(R.string.textPause)
                    }
                    is RoleState.GameIsPause -> {
                        controlGameImageView.setImageResource(R.drawable.play)
                        controlGameTextView.setText(R.string.textContinue)
                    }
                    is RoleState.Player -> {
                        locationButton.isEnabled = false
                    }
                    is RoleState.Spy -> {
                        locationButton.isEnabled = true
                    }
                    is RoleState.Voted -> {
                        voteButton.isEnabled = false
                    }
                }
            }.launchIn(lifecycleScope)

        }

        with(viewModel) {
            setRolesInGame(gameId)
            observeRoleOfCurrentPlayer(gameId)
            observeGame(gameId)
        }
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }
}
