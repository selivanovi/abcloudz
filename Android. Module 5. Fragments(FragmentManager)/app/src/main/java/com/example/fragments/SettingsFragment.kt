package com.example.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val lifecycleObserver = MyLifecycleObserver("SettingsFragment")


    lateinit var toolbar: Toolbar
        private set

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.commit {
            add<Fragment1>(R.id.settingsContainerView)
            setReorderingAllowed(true)
        }
        lifecycle.addObserver(lifecycleObserver)
        configureToolbar(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun configureToolbar(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.settingsToolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}