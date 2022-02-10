package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.viewmodel.WaitingGameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WaitingGameFragment : BaseFragment<WaitingGameViewModel>(R.layout.fragment_waiting_game), LobbyFragmentListener {

    override val viewModel: WaitingGameViewModel by viewModels()

    private val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.nameTextView).text = viewModel.currentUser.name

        childFragmentManager.commit {
            add(
                R.id.waitingGameContainerView,
                LobbyFragment::class.java,
                LobbyFragment.getBundle(gameId)
            )
        }

        viewModel.gameStateChannel.onEach { state ->
            if(state is GameState.ExitToMenu) {
                findNavController().popBackStack(R.id.startFragment, false)
            }

        }.launchIn(lifecycleScope)

        view.findViewById<ImageView>(R.id.back).setOnClickListener {
            lifecycleScope.launch {
                viewModel.clearGame(gameId)
                findNavController().popBackStack()
            }
        }
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }

    override fun startGame() {
        findNavController().navigate(R.id.action_waitingGameFragment_to_roleFragment, getBundle(gameId))
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