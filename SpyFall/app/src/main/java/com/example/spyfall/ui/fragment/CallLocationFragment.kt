package com.example.spyfall.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.databinding.FragmentCallLocationBinding
import com.example.spyfall.ui.base.ResultFragment
import com.example.spyfall.ui.viewmodel.CallLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallLocationFragment :
    ResultFragment<FragmentCallLocationBinding, CallLocationViewModel>(FragmentCallLocationBinding::inflate) {

    override val viewModel: CallLocationViewModel by viewModels()

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }

    override fun setButtonDrawer(): View = binding.menu
}
