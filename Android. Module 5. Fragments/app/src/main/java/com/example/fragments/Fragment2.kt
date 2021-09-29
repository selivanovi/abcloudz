package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment2 : Fragment(R.layout.fragment2) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.beforeFab2).setOnClickListener {
            findNavController().navigate(R.id.fragment1)
        }
        view.findViewById<FloatingActionButton>(R.id.nextFab2).setOnClickListener {
            findNavController().navigate(R.id.action_fragment2_to_fragment3)
        }
    }
}