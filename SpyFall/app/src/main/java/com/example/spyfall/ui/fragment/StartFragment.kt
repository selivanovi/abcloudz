package com.example.spyfall.ui.fragment

import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentStartBinding
import com.example.spyfall.ui.base.DrawerFragment
import com.example.spyfall.ui.listener.JoinGameFragmentListener
import com.example.spyfall.ui.listener.LinkFragmentListener
import com.example.spyfall.ui.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment :
    DrawerFragment<FragmentStartBinding, StartViewModel>(FragmentStartBinding::inflate),
    LinkFragmentListener,
    JoinGameFragmentListener {

    override val viewModel: StartViewModel by viewModels()

    override fun setupView() {
        super.setupView()

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.startGameContainerView, JoinGameFragment())
        }

        binding.nameTextView.text = viewModel.currentPLayer.name

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
        viewModel.navigateToPrepareFragmentWithArgs(gameId)
    }

    override fun joinToGame(gameId: String) {
        viewModel.joinToGame(gameId)
        viewModel.navigateToWaitingGameFragment(gameId)
    }
}
