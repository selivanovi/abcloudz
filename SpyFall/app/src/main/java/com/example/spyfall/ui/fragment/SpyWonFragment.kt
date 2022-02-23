package com.example.spyfall.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.databinding.FragmentSpyWonBinding
import com.example.spyfall.ui.base.GameFragment
import com.example.spyfall.ui.base.ResultFragment
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class SpyWonFragment :
    ResultFragment<FragmentSpyWonBinding>(FragmentSpyWonBinding::inflate) {

    override val viewModel: ResultViewModel by viewModels()

    override fun setupView() {
        super.setupView()
        with(binding) {
            locationImageView.setImageResource(Role.SPY.drawable)
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            nextCardButton.setOnClickListener {
                viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.CONTINUE)
            }
            mainMenuButton.setOnClickListener {
                viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.EXIT)
            }
        }
    }

    override fun setButtonDrawer(): View = binding.menu
}
