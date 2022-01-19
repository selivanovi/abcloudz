package com.example.spyfall.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.google.android.material.button.MaterialButton

class StartGameScreen : Fragment(R.layout.fragment_start_game) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val childNavHost =
            childFragmentManager.findFragmentById(R.id.startGameContainerView) as NavHostFragment
        val childNavController = childNavHost.navController

        val joinButton = view.findViewById<AppCompatButton>(R.id.buttonJoinGame)
        val createGame = view.findViewById<AppCompatButton>(R.id.buttonCreateGame)

        joinButton.isActivated = true

        joinButton.setOnClickListener {
            if (!joinButton.isActivated) {
                Log.d(TAG, "Click join button")
                childNavController.navigate(R.id.joinGameView)
                joinButton.isActivated = true
                createGame.isActivated = false
            }
        }

        createGame.setOnClickListener {
            if (!createGame.isActivated) {
                Log.d(TAG, "Click create button")
                childNavController.navigate(R.id.linkView)
                createGame.isActivated = true
                joinButton.isActivated = false
            }
        }

    }

    companion object {
        private const val TAG = "StartGameScreen"
    }
}