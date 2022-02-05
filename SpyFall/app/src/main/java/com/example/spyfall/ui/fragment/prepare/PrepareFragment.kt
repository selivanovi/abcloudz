package com.example.spyfall.ui.fragment.prepare

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.RoleFragment
import com.example.spyfall.ui.fragment.prepare.sub.LobbyHostFragment
import com.example.spyfall.ui.fragment.prepare.sub.PickTimeFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.listener.PickTimeFragmentListener
import com.example.spyfall.ui.viewmodel.PrepareGameViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepareFragment : BaseFragment(R.layout.fragment_prepare), LobbyFragmentListener, PickTimeFragmentListener {

    override val TAG: String
        get() = "InvitePlayerFragment"

    private val viewModel: PrepareGameViewModel by viewModels()

    private val gameId: String by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    private val buttonPlayers by lazy {
        requireView().findViewById<MaterialButton>(R.id.buttonPlayers)
    }
    private val buttonCardDuration by lazy {
        requireView().findViewById<AppCompatButton>(R.id.buttonCardDuration)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDomain = viewModel.getUser()!!

        view.findViewById<TextView>(R.id.nameTextView).text = userDomain.name
        view.findViewById<TextView>(R.id.gameIdTextView).text =
            resources.getString(R.string.textWelcomeToTheGameWithId, gameId)

        childFragmentManager.commit {
            add(R.id.createGameContainerView, LobbyHostFragment.newInstance(gameId))
        }

        view.findViewById<TextView>(R.id.nameTextView).text = userDomain.name

        createButtons()

        viewModel.createGame(gameId, userDomain)
    }

    override fun startGame() {
        findNavController().navigate(
            R.id.action_prepareFragment_to_roleFragment,
            RoleFragment.getBundle(gameId)
        )
    }

    private fun createButtons() {

        buttonPlayers.isActivated = true

        buttonPlayers.setOnClickListener {
            if (!buttonPlayers.isActivated) {
                Log.d(TAG, "Click players button")
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
                Log.d(TAG, "Click time button")
                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.createGameContainerView, PickTimeFragment())
                }
                buttonCardDuration.isActivated = true
                buttonPlayers.isActivated = false
            }
        }
    }

    override fun setTime(time: Int) {
        viewModel.setTimeForGame(gameId, time)
        buttonPlayers.callOnClick()
    }

    companion object {

        private const val TAG = "InvitePLayerFragment"

        private const val KEY_GAME_ID = "key_game_id"

        fun getBundle(gameId: String): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
            }
        }
    }
}