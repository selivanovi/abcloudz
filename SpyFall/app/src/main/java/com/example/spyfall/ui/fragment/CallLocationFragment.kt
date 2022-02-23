package com.example.spyfall.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import com.example.spyfall.databinding.FragmentCallLocationBinding
import com.example.spyfall.ui.base.CheckFragment
import com.example.spyfall.ui.viewmodel.CallLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLocationFragment :
    CheckFragment<FragmentCallLocationBinding, CallLocationViewModel>(FragmentCallLocationBinding::inflate) {

    override val viewModel: CallLocationViewModel by viewModels()

    override fun setButtonDrawer(): View = binding.menu
}
