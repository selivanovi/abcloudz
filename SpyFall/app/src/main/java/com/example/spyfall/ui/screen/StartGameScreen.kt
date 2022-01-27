package com.example.spyfall.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.screen.listener.CreateGameListener

class StartGameScreen : Fragment(R.layout.fragment_start_game), CreateGameListener {


    private val user: User by lazy {
        requireArguments().getSerializable(KEY_USER)!! as User
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val childNavHost =
            childFragmentManager.findFragmentById(R.id.startGameContainerView) as NavHostFragment
        val childNavController = childNavHost.navController

        val joinButton = view.findViewById<AppCompatButton>(R.id.buttonJoinGame)
        val createGameButton = view.findViewById<AppCompatButton>(R.id.buttonCreateGame)

        view.findViewById<TextView>(R.id.nameTextView).text = user.name

        joinButton.isActivated = true
        childNavController.navigate(R.id.joinGameView)

        joinButton.setOnClickListener {
            if (!joinButton.isActivated) {
                Log.d(TAG, "Click join button")
                childNavController.navigate(R.id.joinGameView)
                joinButton.isActivated = true
                createGameButton.isActivated = false
            }
        }

        createGameButton.setOnClickListener {
            if (!createGameButton.isActivated) {
                Log.d(TAG, "Click create button")
                childNavController.navigate(R.id.linkView)
                createGameButton.isActivated = true
                joinButton.isActivated = false
            }
        }

    }

    override fun createGame(gameId: String) {
        findNavController().navigate(R.id.action_startGameScreen_to_invitePlayerScreen, InvitePlayerScreen.getInstance(user, gameId))
    }

    companion object {
        private const val TAG = "StartGameScreen"
        private const val KEY_USER = "key_user"

        fun getInstance(user: User): Bundle {
            return Bundle().apply {
                putSerializable(KEY_USER, user)
            }
        }
    }
}