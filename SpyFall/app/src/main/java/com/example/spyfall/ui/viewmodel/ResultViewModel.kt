package com.example.spyfall.ui.viewmodel


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.User
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.utils.times
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
) : BaseViewModel() {

    private val currentPlayer: User = userRepository.getUser()!!

    private val resultStateMutableChannel = Channel<ResultState>()
    val resultStateChannel = resultStateMutableChannel.receiveAsFlow()

    private val roleMutableChannel = Channel<Role>()
    val roleChannel = roleMutableChannel.receiveAsFlow()

    fun observeStatusOfCurrentPlayer(gameId: String) {
        launch {
            gameRepository.observePlayerInGame(gameId, currentPlayer.userId).collect { result ->
                result.onSuccess { player ->
                    if (player.status == PlayerStatus.EXIT)
                        deletePlayerInGame(gameId, player)
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
                if (players.size < Constants.MIN_NUMBER_PLAYERS)
                    resultStateMutableChannel.send(ResultState.Exit)
                if (players.all { player -> player.status == PlayerStatus.Continue }) {
                    gameRepository.setStatusForGame(gameId, GameStatus.PLAYING)
                    resetPlayerInGame(gameId)
                    resultStateMutableChannel.send(ResultState.Continue)
                }
                val player = players.find { player -> player.role != Role.SPY } ?: return@onEach
                player.role?.let { role ->
                    roleMutableChannel.send(role)
                }
            }
            result.onFailure { throwable -> errorMutableChannel.send(throwable) }
        }.launchIn(viewModelScope)
    }

    private fun resetPlayerInGame(gameId: String) {

        val isHost = async { getHost(gameId) == currentPlayer.userId }

        launch {
            if (isHost.await()) return@launch
            val players = gameRepository.getPlayersFromGame(gameId)
            players.forEach { player ->
                val newPlayer =
                    PlayerDomain(player.playerId, player.name, PlayerStatus.PLAY, null, null)
                gameRepository.updatePlayerInGame(gameId, newPlayer)
            }
        }
    }

    private fun deletePlayerInGame(gameId: String, playerDomain: PlayerDomain) {
        val isHost = async { getHost(gameId) == playerDomain.playerId }
        launch {
            gameRepository.deletePlayerInGame(gameId, currentPlayer.userId)
            if (isHost.await()) {
                gameRepository.deleteGame(gameId)
            }
        }
    }

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId).gameId
    }

    fun setStatusForCurrentPlayerInGame(gameId: String, status: PlayerStatus) {
        launch {
            gameRepository.setStatusForPlayerInGame(gameId, currentPlayer.userId, status)
        }
    }
}

sealed class ResultState {

    object Exit : ResultState()
    object Continue : ResultState()
}