package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.ui.state.CheckState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckLocationViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    private val checkStateMutableChannel = Channel<CheckState>()
    val checkStateChannel = checkStateMutableChannel.receiveAsFlow()

    fun observeGame(gameId: String) {
            launch {
                gameRepository.observeGame(gameId).collect { result ->
                    result.onSuccess { game ->
                        if (game?.status != null) {
                            if (game.status == GameStatus.SPY_WON)
                                checkStateMutableChannel.send(CheckState.SpyWon)
                            if (game.status == GameStatus.LOCATION_WON)
                                checkStateMutableChannel.send(CheckState.SpyLost)
                        }
                    }
                    result.onFailure { throwable ->
                        errorMutableChannel.send(throwable)
                    }
                }
            }
    }

    fun setStatusForGame(gameId: String, status: GameStatus) {
        launch {
            gameRepository.setStatusForGame(gameId, status)
        }
    }
}

