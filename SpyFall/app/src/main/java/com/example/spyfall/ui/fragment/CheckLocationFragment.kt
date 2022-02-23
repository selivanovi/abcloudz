package com.example.spyfall.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.databinding.FragmentCheckLocationBinding
import com.example.spyfall.ui.base.CheckFragment
import com.example.spyfall.ui.viewmodel.CheckLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckLocationFragment :
    CheckFragment<FragmentCheckLocationBinding, CheckLocationViewModel>(FragmentCheckLocationBinding::inflate) {

    override val viewModel: CheckLocationViewModel by viewModels()

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            buttonCorrectly.setOnClickListener {
                viewModel.setStatusForGame(gameId, GameStatus.SPY_WON)
            }
            buttonNo.setOnClickListener {
                viewModel.setStatusForGame(gameId, GameStatus.LOCATION_WON)
            }
        }
    }

    override fun setButtonDrawer(): View = binding.menu
}
