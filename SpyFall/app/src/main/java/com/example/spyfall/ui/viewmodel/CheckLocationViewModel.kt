package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.CheckResultViewModel
import com.example.spyfall.ui.navigation.CheckLocationDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckLocationViewModel @Inject constructor(
    gameRepository: GameRepository,
    userRepository: UserRepository,
) : CheckResultViewModel(gameRepository, userRepository) {

    override fun navigateToSpyWon(gameId: String) {
        navigateTo(CheckLocationDirections.toSpyWonWithArgs(gameId))
    }

    override fun navigateToLocationWon(gameId: String) {
        navigateTo(CheckLocationDirections.toLocationWonWithArgs(gameId))
    }
}
