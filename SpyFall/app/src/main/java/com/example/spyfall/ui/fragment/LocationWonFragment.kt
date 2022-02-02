package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.spyfall.R

class LocationWonFragment : Fragment(R.layout.fragment_location_won) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.locationButton).setOnClickListener {
        }
    }
}