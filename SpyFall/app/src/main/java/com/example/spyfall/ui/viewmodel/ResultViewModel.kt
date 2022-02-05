package com.example.spyfall.ui.viewmodel


import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.ResultState
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
) : BaseViewModel() {

    private val currentPlayer: UserDomain = userRepository.getUser()!!

    private val resultStateMutableChannel = Channel<ResultState>()
    val resultStateChannel = resultStateMutableChannel.receiveAsFlow()

    private val roleMutableChannel = Channel<Role>()
    val roleChannel = roleMutableChannel.receiveAsFlow()

    fun observeStatusOfCurrentPlayer(gameId: String) {
        launch {
            gameRepository.observePlayerFromGame(gameId, currentPlayer.userId).collect { result ->
                result.onSuccess { player ->
                    if (player.status == PlayerStatus.EXIT) {
                        deletePlayerInGame(gameId, player.playerId)
                        resultStateMutableChannel.send(ResultState.Exit)
                        val isHost = async { checkHost(gameId, currentPlayer.userId) }
                        if (isHost.await()) {
                            deleteGameById(gameId)
                        } else {
                            deletePlayerInGame(gameId, player.playerId)
                        }
                    }
                    if (player.status == PlayerStatus.Continue) {
                        val isHost = async { checkHost(gameId, currentPlayer.userId) }
                        if (isHost.await()) {
                            resultStateMutableChannel.send(ResultState.HostContinue)
                        } else {
                            resultStateMutableChannel.send(ResultState.PlayerContinue)
                        }
                    }
                }
                result.onFailure { throwable ->
                    errorMutableChannel.send(throwable)
                }
            }
        }
    }

    fun observeStatusOfPlayers(gameId: String) {
        gameRepository.observePlayersFromGame(gameId).onEach { result ->
            result.onSuccess { players ->
                val player = players.find { player -> player.role != Role.SPY } ?: return@onEach
                player.role?.let { role ->
                    roleMutableChannel.send(role)
                }
            }
            result.onFailure { throwable -> errorMutableChannel.send(throwable) }
        }.launchIn(viewModelScope)
    }

    private  suspend fun deletePlayerInGame(gameId: String, playerId: String) {
        gameRepository.deletePlayerInGame(gameId, playerId)
    }

    private suspend fun deleteGameById(gameId: String) {

        gameRepository.deleteGame(gameId)
    }

    private suspend fun checkHost(gameId: String, playerId: String): Boolean {
        return getHost(gameId) == playerId
    }

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId).host!!
    }

    fun setStatusForCurrentPlayerInGame(gameId: String, status: PlayerStatus) {
        launch {
            gameRepository.setStatusForPlayerInGame(gameId, currentPlayer.userId, status)
        }
    }
}

