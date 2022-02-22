package com.example.spyfall.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.databinding.FragmentRoleBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.state.RoleState
import com.example.spyfall.ui.viewmodel.RoleViewModel
import com.example.spyfall.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
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

            viewModel.roleChannel.onEach { role ->
                locationTextView.setText(role.string)
                locationImageView.setImageResource(role.drawable)
            }.launchIn(lifecycleScope)

            viewModel.timeChannel.onEach { duration ->

                binding.timeTextView.text =
                    resources.getString(
                        R.string.time,
                        duration.inWholeMinutes,
                        duration.inWholeSeconds
                    )
            }.launchIn(lifecycleScope)

            viewModel.roleStateChannel.onEach { state ->
                when (state) {
                    is RoleState.GameIsPlaying -> {
                        controlGameImageView.setImageResource(R.drawable.pause)
                        controlGameTextView.setText(R.string.textPause)
                    }
                    is RoleState.GameIsPause -> {
                        controlGameImageView.setImageResource(R.drawable.play)
                        controlGameTextView.setText(R.string.textContinue)
                    }
                    is RoleState.Player -> locationButton.isEnabled = false
                    is RoleState.Spy -> locationButton.isEnabled = true
                    is RoleState.Voted -> voteButton.isEnabled = false
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
