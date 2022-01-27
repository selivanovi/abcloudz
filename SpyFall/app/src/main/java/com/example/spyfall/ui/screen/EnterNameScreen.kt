package com.example.spyfall.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.viewmodel.SetNameViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EnterNameScreen : Fragment(R.layout.fragment_enter_name) {

    private val viewModel: SetNameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()?.let {
            findNavController().navigate(R.id.action_enterNameScreen_to_startGameScreen, StartGameScreen.getInstance(it))
        }

        viewModel.errorChannel.onEach {
            Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }.launchIn(lifecycleScope)

        viewModel.successAuthorizationChannel.onEach {
            findNavController().navigate(R.id.action_enterNameScreen_to_startGameScreen,
                viewModel.getUser()?.let { user -> StartGameScreen.getInstance(user) })
        }.launchIn(lifecycleScope)


        createButtons(view)
    }

    private fun createButtons(view: View) {
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
                viewModel.logIn(nameEditText.text.toString())
            }
        }
    }
}