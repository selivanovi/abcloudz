package com.example.spyfall.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.MainActivity
import com.example.spyfall.R

class EnterNameScreen : Fragment(R.layout.fragment_enter_name) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmButton = view.findViewById<Button>(R.id.buttonConfirm)
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)

        nameEditText.apply {
            addTextChangedListener {
                confirmButton.isEnabled = nameEditText.text.isNotEmpty()
            }
        }

        confirmButton.apply {
            isEnabled = false
            setOnClickListener {

                findNavController().navigate(R.id.action_enterNameScreen_to_startGameScreen)
            }
        }
    }
}