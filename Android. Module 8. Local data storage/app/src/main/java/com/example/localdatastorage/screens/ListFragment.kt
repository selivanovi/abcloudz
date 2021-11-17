package com.example.localdatastorage.screens

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.localdatastorage.R
import com.example.localdatastorage.utils.DonutJsonParser

class ListFragment : Fragment(R.layout.fragment_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<TextView>(R.id.textView).setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_detailsFragment)
//        }
    }
}