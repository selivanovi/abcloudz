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
import com.example.spyfall.databinding.FragmentLogInBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.base.DrawerFragment
import com.example.spyfall.ui.viewmodel.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LogInFragment :
    DrawerFragment<FragmentLogInBinding, LogInViewModel>(FragmentLogInBinding::inflate) {

    override val viewModel: LogInViewModel by viewModels()

    override fun setupView() {
        super.setupView()
        viewModel.getUser()?.let {
            viewModel.navigateToStartFragment()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.successAuthorizationChannel.onEach {
            viewModel.navigateToStartFragment()
        }.launchIn(lifecycleScope)
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {

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
}