package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.navigation.WaitingGameDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val waitingGameDirections: WaitingGameDirections
) : GameViewModel(gameRepository, userRepository) {

    fun navigateToRoleWithArgs(gameId: String) {
        navigateTo(waitingGameDirections.toRoleWithArgs(gameId))
    }
}
