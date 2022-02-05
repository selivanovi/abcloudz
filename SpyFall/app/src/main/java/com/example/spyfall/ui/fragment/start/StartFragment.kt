package com.example.spyfall.ui.fragment.start

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.prepare.PrepareFragment
import com.example.spyfall.ui.fragment.start.sub.JoinGameFragment
import com.example.spyfall.ui.fragment.start.sub.LinkFragment
import com.example.spyfall.ui.listener.JoinGameFragmentListener
import com.example.spyfall.ui.listener.LinkFragmentListener
import com.example.spyfall.ui.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : BaseFragment(R.layout.fragment_start), LinkFragmentListener,
    JoinGameFragmentListener {

    override val TAG: String
        get() = "StartGameFragment"

    private val viewModel: StartViewModel by viewModels()

    private val userDomain: UserDomain by lazy {
        viewModel.getUser()!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.startGameContainerView, JoinGameFragment())
        }

        view.findViewById<TextView>(R.id.nameTextView).text = userDomain.name

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
                Log.d(TAG, "Click join button")
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
                Log.d(TAG, "Click create button")
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
        findNavController().navigate(R.id.action_startFragment_to_waitingGameFragment)
    }
}