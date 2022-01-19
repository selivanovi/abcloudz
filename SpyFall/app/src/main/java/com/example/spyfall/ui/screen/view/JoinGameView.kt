package com.example.spyfall.ui.screen.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R

class JoinGameView : Fragment(R.layout.fragment_join_game_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonJoinGame).setOnClickListener {
            findNavController().navigate(R.id.action_joinGameView_to_waitingGameView)
        }
    }
}