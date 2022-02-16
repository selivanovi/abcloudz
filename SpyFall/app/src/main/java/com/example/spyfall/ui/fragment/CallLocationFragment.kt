package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentCallLocationBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.state.CheckState
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.viewmodel.CallLocationViewModel
import com.example.spyfall.ui.viewmodel.CheckLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallLocationFragment :
    GameFragment<FragmentCallLocationBinding, CallLocationViewModel>(FragmentCallLocationBinding::inflate) {

    override val viewModel: CallLocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeGameStatusForResult(gameId)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            viewModel.clearGame(gameId)
        }
    }
}