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
    private val userRepository: UserRepository,
) : GameViewModel(gameRepository, userRepository) {

    fun observeGameStatusForResult(gameId: String) {
        gameRepository.observeGame(gameId).onEach { game ->
            if (game?.status != null) {
                if (game.status == GameStatus.SPY_WON) {
                    navigateToSpyWonWithArgs(gameId)
                }
                if (game.status == GameStatus.LOCATION_WON) {
                    navigateToLocationWonWithArgs(gameId)
                }
            }
        }.launchIn(viewModelScope)
    }

    abstract fun navigateToSpyWonWithArgs(gameId: String)

    abstract fun navigateToLocationWonWithArgs(gameId: String)
}
