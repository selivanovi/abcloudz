package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            val mainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            mainViewModel.changeTitle("Home")
        }

        super.onViewCreated(view, savedInstanceState)
    }
}