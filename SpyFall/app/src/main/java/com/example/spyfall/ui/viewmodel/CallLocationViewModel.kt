package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.CheckResultViewModel
import com.example.spyfall.ui.navigation.CallLocationDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallLocationViewModel @Inject constructor(
    gameRepository: GameRepository,
    userRepository: UserRepository,
    private val callLocationDirections: CallLocationDirections
) : CheckResultViewModel(gameRepository, userRepository) {

    override fun navigateToSpyWonWithArgs(gameId: String) {
        navigateTo(callLocationDirections.toSpyWonWithArgs(gameId))
    }

    override fun navigateToLocationWonWithArgs(gameId: String) {
        navigateTo(callLocationDirections.toLocationWonWithArgs(gameId))
    }
}
