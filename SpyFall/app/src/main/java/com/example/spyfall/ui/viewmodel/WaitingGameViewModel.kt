package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.navigation.WaitingGameDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WaitingGameViewModel @Inject constructor(
    gameRepository: GameRepository,
    userRepository: UserRepository,
) : GameViewModel(gameRepository, userRepository) {

    fun navigateToRoleWithArgs(gameId: String) {
        navigateTo(WaitingGameDirections.toRoleWithArgs(gameId))
    }
}
