package com.example.spyfall.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.screen.listener.StartGameListener
import com.example.spyfall.ui.viewmodel.CreateGameViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitePlayerScreen : Fragment(R.layout.fragment_invite_player), StartGameListener {

    private val viewModel: CreateGameViewModel by viewModels()

    private val user: User by lazy {
        requireArguments().getSerializable(KEY_USER)!! as User
    }

    private val gameId: String by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.nameTextView).text = user.name
        view.findViewById<TextView>(R.id.gameIdTextView).text =
            resources.getString(R.string.textWelcomeToTheGameWithId, gameId)

        val childNavHost =
            childFragmentManager.findFragmentById(R.id.createGameContainerView) as NavHostFragment
        val childNavController = childNavHost.navController

        val buttonPlayers = view.findViewById<MaterialButton>(R.id.buttonPlayers)
        val buttonCardDuration = view.findViewById<AppCompatButton>(R.id.buttonCardDuration)

        view.findViewById<TextView>(R.id.nameTextView).text = user.name

        buttonPlayers.isActivated = true
        childNavController.navigate(R.id.invitePlayerView)

        buttonPlayers.setOnClickListener {
            if (!buttonPlayers.isActivated) {
                Log.d(TAG, "Click players button")
                childNavController.navigate(R.id.invitePlayerView)
                buttonPlayers.isActivated = true
                buttonCardDuration.isActivated = false
            }
        }

        buttonCardDuration.setOnClickListener {
            if (!buttonCardDuration.isActivated) {
                Log.d(TAG, "Click time button")
                childNavController.navigate(R.id.pickTimeView)
                buttonCardDuration.isActivated = true
                buttonPlayers.isActivated = false
            }
        }

        viewModel.createGame(gameId, user)
    }

    override fun startGame() {
        findNavController().navigate(
            R.id.roleScreen,
            RoleScreen.getInstance(gameId, true)
        )
    }

    companion object {

        private const val TAG = "InvitePLayerScreen"

        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_USER = "key_game"

        fun getInstance(user: User, gameId: String): Bundle {
            return Bundle().apply {
                putSerializable(KEY_USER, user)
                putString(KEY_GAME_ID, gameId)
            }
        }
    }

}