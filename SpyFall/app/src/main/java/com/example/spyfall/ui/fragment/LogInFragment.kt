package com.example.spyfall.ui.fragment

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.databinding.FragmentLogInBinding
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
            nameEditText.addTextChangedListener {
                confirmButton.isEnabled = nameEditText.text.isNotEmpty()
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
