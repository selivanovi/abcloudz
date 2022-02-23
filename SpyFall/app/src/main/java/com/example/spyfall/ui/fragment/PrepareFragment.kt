package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentPrepareBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.listener.PickTimeFragmentListener
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.PrepareState
import com.example.spyfall.ui.viewmodel.PrepareGameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrepareFragment :
    GameFragment<FragmentPrepareBinding, PrepareGameViewModel>(FragmentPrepareBinding::inflate),
    LobbyFragmentListener,
    PickTimeFragmentListener {

    override val viewModel: PrepareGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupGame(gameId)
    }

    override fun setupView() {
        super.setupView()

        childFragmentManager.commit {
            add(R.id.createGameContainerView, WaitingFragment())
        }

        with(binding) {
            nameTextView.text = viewModel.currentUser.name
            gameIdTextView.text =
                resources.getString(R.string.textWelcomeToTheGameWithId, gameId)
        }

        createButtons()
    }

    override fun setupListeners() {
        super.setupListeners()

        binding.back.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clearGame(gameId)
                findNavController().navigateUp()
            }
        }
    }

    override fun setupObserver() {

        viewModel.prepareStateChannel.onEach { state ->
            if (state is PrepareState.GameIsReady){
                childFragmentManager.commit {
                    replace(R.id.createGameContainerView, LobbyHostFragment.newInstance(gameId))
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.errorChannel.onEach { throwable ->
            Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_LONG).show()
        }.launchIn(lifecycleScope)

        viewModel.gameStateChannel.onEach { state ->
            if (state is GameState.ExitToMenu) {
                findNavController().navigateUp()
            }
        }.launchIn(lifecycleScope)

        viewModel.observeGameExit(gameId)
    }

    override fun startGame() {
        viewModel.navigateToRole(gameId)
    }

    private fun createButtons() {
        with(binding) {
            buttonPlayers.isActivated = true

            buttonPlayers.setOnClickListener {
                if (!buttonPlayers.isActivated) {
                    childFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.createGameContainerView, LobbyHostFragment.newInstance(gameId))
                    }
                    buttonPlayers.isActivated = true
                    buttonCardDuration.isActivated = false
                }
            }

            buttonCardDuration.setOnClickListener {
                if (!buttonCardDuration.isActivated) {
                    childFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.createGameContainerView, PickTimeFragment())
                    }
                    buttonCardDuration.isActivated = true
                    buttonPlayers.isActivated = false
                }
            }
        }
    }

    override fun setTime(time: Long) {
        viewModel.setDuration(gameId, time)
        binding.buttonPlayers.callOnClick()
    }

    override fun setButtonDrawer(): View = binding.menu
}
