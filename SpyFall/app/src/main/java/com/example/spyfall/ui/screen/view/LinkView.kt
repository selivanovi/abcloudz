package com.example.spyfall.ui.screen.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R

class LinkView : Fragment(R.layout.fragment_link_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.buttonCool).setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController().navigate(R.id.action_startGameScreen_to_invitePlayerScreen)
        }
    }
}