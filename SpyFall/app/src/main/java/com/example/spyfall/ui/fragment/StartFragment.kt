package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.listener.JoinGameFragmentListener
import com.example.spyfall.ui.listener.LinkFragmentListener
import com.example.spyfall.ui.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StartFragment : BaseFragment<StartViewModel>(R.layout.fragment_start), LinkFragmentListener,
    JoinGameFragmentListener {

    override val viewModel: StartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.startGameContainerView, JoinGameFragment())
        }

        viewModel.errorChannel.onEach {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }.launchIn(lifecycleScope)

        view.findViewById<TextView>(R.id.nameTextView).text = viewModel.currentPLayer.name

        createButtons()
    }

    private fun createButtons() {

        val joinButton =
            requireView().findViewById<AppCompatButton>(R.id.buttonJoinGame)

        val createGameButton =
            requireView().findViewById<AppCompatButton>(R.id.buttonCreateGame)

        joinButton.isActivated = true

        joinButton.setOnClickListener {
            if (!joinButton.isActivated) {
                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.startGameContainerView, JoinGameFragment())
                }
                joinButton.isActivated = true
                createGameButton.isActivated = false
            }
        }

        createGameButton.setOnClickListener {
            if (!createGameButton.isActivated) {
                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.startGameContainerView, LinkFragment())
                }
                createGameButton.isActivated = true
                joinButton.isActivated = false
            }
        }
    }

    override fun createGame(gameId: String) {
        findNavController().navigate(
            R.id.action_startFragment_to_prepareFragment,
            PrepareFragment.getBundle(gameId)
        )
    }

    override fun joinToGame(gameId: String) {
        viewModel.joinToGame(gameId)
        findNavController().navigate(R.id.action_startFragment_to_waitingGameFragment, WaitingGameFragment.getBundle(gameId))
    }
}