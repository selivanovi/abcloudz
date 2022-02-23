package com.example.spyfall.ui.fragment

import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentWaitingGameBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.viewmodel.WaitingGameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WaitingGameFragment :
    GameFragment<FragmentWaitingGameBinding, WaitingGameViewModel>(FragmentWaitingGameBinding::inflate),
    LobbyFragmentListener {

    override val viewModel: WaitingGameViewModel by viewModels()

    override fun setupView() {
        super.setupView()

        binding.nameTextView.text = viewModel.currentUser.name

        childFragmentManager.commit {
            add(
                R.id.waitingGameContainerView,
                LobbyFragment::class.java,
                LobbyFragment.getBundle(gameId)
            )
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.back.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clearGame(gameId)
                viewModel.navigateBack()
            }
        }
    }

    override fun setupObserver() {
        viewModel.errorChannel.onEach { throwable ->
            Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_LONG).show()
        }.launchIn(lifecycleScope)

        viewModel.gameStateChannel.onEach { state ->
            if (state is GameState.ExitToMenu) {
                findNavController().popBackStack(R.id.startFragment, false)
            }
        }.launchIn(lifecycleScope)

        viewModel.observeGameExit(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }

    override fun startGame() {
        viewModel.navigateToRole(gameId)
    }
}
