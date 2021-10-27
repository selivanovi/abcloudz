package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment1 : Fragment(R.layout.fragment1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (parentFragment as SettingsFragment).toolbar.title = "Fragment 1"
        view.findViewById<FloatingActionButton>(R.id.nextFab1).setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<Fragment2>(R.id.settingsContainerView)
                addToBackStack(null)
            }
        }
    }

}