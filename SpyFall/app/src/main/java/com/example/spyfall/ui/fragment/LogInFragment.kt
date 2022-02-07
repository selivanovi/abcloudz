package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.viewmodel.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LogInFragment : BaseFragment(R.layout.fragment_log_in) {

    override val TAG: String
        get() = "LogInFragment"

    private val viewModel: LogInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()?.let {
            findNavController().navigate(R.id.action_logInFragment_to_startFragment)
        }

        viewModel.errorChannel.onEach { throwable ->
            Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_LONG).show()
        }.launchIn(lifecycleScope)

        viewModel.successAuthorizationChannel.onEach {
            findNavController().navigate(R.id.action_logInFragment_to_startFragment)
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