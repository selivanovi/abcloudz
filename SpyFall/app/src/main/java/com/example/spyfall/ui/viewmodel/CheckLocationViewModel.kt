package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.CheckResultViewModel
import com.example.spyfall.ui.navigation.CheckLocationDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckLocationViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val checkLocationDirections: CheckLocationDirections,
) : CheckResultViewModel(gameRepository, userRepository) {

    override fun navigateToSpyWonWithArgs(gameId: String) {
        navigateTo(checkLocationDirections.toSpyWonWithArgs(gameId))
    }

    override fun navigateToLocationWonWithArgs(gameId: String) {
        navigateTo(checkLocationDirections.toLocationWonWithArgs(gameId))
    }
}
