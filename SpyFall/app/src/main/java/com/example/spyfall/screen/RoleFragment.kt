package com.example.spyfall.screen

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.spyfall.R

class RoleFragment : Fragment(R.layout.fragment_role) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.locationButton).setOnClickListener {
        }
    }
}