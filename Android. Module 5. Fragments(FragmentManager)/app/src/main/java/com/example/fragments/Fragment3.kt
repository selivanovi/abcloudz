package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment3 : Fragment(R.layout.fragment3) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (parentFragment as SettingsFragment).toolbar.title = "Fragment 3"
        view.findViewById<FloatingActionButton>(R.id.beforeFab3).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}