package com.example.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScreenSlidePageFragment : Fragment(R.layout.screen_slide_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.textView)
        arguments?.let {
            textView.text = it.getString(ARG_STRING)
        }
    }

    companion object {
        const val ARG_STRING = "arg_string"
    }
}
