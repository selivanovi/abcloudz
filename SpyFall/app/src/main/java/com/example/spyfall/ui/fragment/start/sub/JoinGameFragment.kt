package com.example.spyfall.ui.fragment.start.sub

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R

class JoinGameFragment : Fragment(R.layout.fragment_join_game) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonJoinGame).setOnClickListener {
        }
    }
}