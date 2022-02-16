package com.example.spyfall.ui.base

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.CheckState
import com.example.spyfall.ui.viewmodel.GameViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class CheckResultViewModel (
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) : GameViewModel(gameRepository, userRepository) {

    private val checkStateMutableChannel = Channel<CheckState>()
    val checkStateChannel = checkStateMutableChannel.receiveAsFlow()

    fun observeGameStatusForResult(gameId: String) {
            launch {
                gameRepository.observeGame(gameId).collect { result ->
                    result.onSuccess { game ->
                        if (game?.status != null) {
                            if (game.status == GameStatus.SPY_WON)
                                navigateToSpyWonWithArgs(gameId)
                            if (game.status == GameStatus.LOCATION_WON)
                                navigateToLocationWonWithArgs(gameId)
                        }
                    }
                    result.onFailure { throwable ->
                        errorMutableChannel.send(throwable)
                    }
                }
            }
    }

    abstract fun navigateToSpyWonWithArgs(gameId: String)

    abstract fun navigateToLocationWonWithArgs(gameId: String)
}

