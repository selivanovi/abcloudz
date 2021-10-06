package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment2 : Fragment(R.layout.fragment2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<FloatingActionButton>(R.id.beforeFab2).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        view.findViewById<FloatingActionButton>(R.id.nextFab2).setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<Fragment3>(R.id.settingsContainerView)
                addToBackStack(null)
            }
        }
    }
}