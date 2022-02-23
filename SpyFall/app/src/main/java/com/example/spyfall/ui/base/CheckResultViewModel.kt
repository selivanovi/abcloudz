package com.example.spyfall.ui.base

import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class CheckResultViewModel(
    private val gameRepository: GameRepository,
    userRepository: UserRepository,
) : GameViewModel(gameRepository, userRepository) {

    fun observeGameStatusForResult(gameId: String) {
        gameRepository.observeGame(gameId).onEach { game ->
            if (game?.status == GameStatus.SPY_WON) {
                navigateToSpyWon(gameId)
            }
            if (game?.status == GameStatus.LOCATION_WON) {
                navigateToLocationWon(gameId)
            }
        }.launchIn(viewModelScope)
    }

    abstract fun navigateToSpyWon(gameId: String)

    abstract fun navigateToLocationWon(gameId: String)
}
