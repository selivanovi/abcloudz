package com.example.spyfall.ui.viewmodel


import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.ResultState
import com.example.spyfall.utils.toPlayerDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : GameViewModel(gameRepository, userRepository) {

    private val resultStateMutableChannel = Channel<ResultState>()
    val resultStateChannel = resultStateMutableChannel.receiveAsFlow()

    fun observeStatusOfCurrentPlayer(gameId: String) {
        gameRepository.observePlayerFromGame(gameId, currentUser.userId).onEach { result ->
            result.onSuccess { player ->
                if (player.status == PlayerStatus.EXIT) {
                    val isHost = checkHost(gameId, currentUser.userId)
                    if (isHost) {
                        deleteGameById(gameId)
                    } else {
                        deletePlayerInGame(gameId, player.playerId)
                    }
                    resultStateMutableChannel.send(ResultState.Exit)
                }
                if (player.status == PlayerStatus.Continue) {
                    resetCurrentPlayer(gameId)
                    val isHost = checkHost(gameId, currentUser.userId)
                    if (isHost) {
                        resultStateMutableChannel.send(ResultState.HostContinue)
                    } else {
                        resultStateMutableChannel.send(ResultState.PlayerContinue)
                    }
                }
            }
            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun resetCurrentPlayer(gameId: String) {
        val player = currentUser.toPlayerDomain()
        gameRepository.addPlayerToGame(gameId, player)
    }

    fun observeStatusOfPlayers(gameId: String) {
        gameRepository.observePlayersFromGame(gameId).onEach { result ->
            result.onSuccess { players ->
                val player = players.find { player -> player.role != Role.SPY } ?: return@onEach
                player.role?.let { role ->
                    resultStateMutableChannel.send(ResultState.SetRole(role))
                }
            }
            result.onFailure { throwable -> errorMutableChannel.send(throwable) }
        }.launchIn(viewModelScope)
    }
}

