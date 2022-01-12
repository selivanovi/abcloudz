package com.example.spyfall.screen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.spyfall.MainActivity
import com.example.spyfall.R

class EnterNameScreen : Fragment(R.layout.fragment_enter_name) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menu = view.findViewById<ImageView>(R.id.menu)

        menu.setOnClickListener {

            (requireActivity() as MainActivity).moveDrawableLayout()

        }
    }
}