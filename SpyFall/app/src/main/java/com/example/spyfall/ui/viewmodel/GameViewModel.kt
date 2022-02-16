package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.state.GameState
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.FewPlayersException
import com.example.spyfall.utils.GameNotFoundException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class GameViewModel(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val currentUser = userRepository.getUser()!!

    private val gameStateMutableChannel = Channel<GameState>()
    val gameStateChannel = gameStateMutableChannel.receiveAsFlow()

    private var currentGame: GameDomain? = null

    fun observeGameExit(gameId: String) {
        gameRepository.observeGame(gameId).onEach {
            it.onSuccess { game ->
                if (currentGame != game) {
                    if (game == null) {
                        gameStateMutableChannel.send(GameState.ExitToMenu)
                    } else {
                        currentGame = game
                    }
                }
            }
            it.onFailure { throwable ->
                errorMutableChannel.send(GameNotFoundException(Constants.GAME_NOT_FOUND_EXCEPTION))
            }
        }.launchIn(viewModelScope)
    }

    fun observeNumberOfPlayer(gameId: String) {
        gameRepository.observePlayersFromGame(gameId).onEach {
            it.onSuccess { players ->
                if (players.size < Constants.MIN_NUMBER_PLAYERS) {
                    it.onSuccess { players ->
                        clearStatusForPLayers(gameId, players)
                        val isHost = checkHost(gameId, currentUser.userId)
                        if (isHost) gameStateMutableChannel.send(GameState.ExitToLobbyForHost)
                        else gameStateMutableChannel.send(GameState.ExitToLobbyForPlayer)
                    }
                }
            }
            it.onFailure { throwable ->
                errorMutableChannel.send(FewPlayersException(Constants.FEW_PLAYERS_EXCEPTION))
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun clearStatusForPLayers(gameId: String, players: List<PlayerDomain>) {
        players.forEach {
            val newPlayer = PlayerDomain(playerId = it.playerId, name = it.name, null, null, null)
            gameRepository.addPlayerToGame(gameId, newPlayer)
        }
    }

    suspend fun clearGame(gameId: String) {
        val isHost = checkHost(gameId, currentUser.userId)

        if (isHost) {
            deleteGameById(gameId)
        } else {
            deletePlayerInGame(gameId, currentUser.userId)
        }
    }

    protected suspend fun setDurationForGame(gameId: String, time: Long) {
        gameRepository.setDurationForGames(gameId, time)
    }

    protected suspend fun deletePlayerInGame(gameId: String, playerId: String) {
        gameRepository.deletePlayerInGame(gameId, playerId)
    }

    protected suspend fun deleteGameById(gameId: String) {
        gameRepository.deleteGame(gameId)
    }

    protected suspend fun checkHost(gameId: String, playerId: String): Boolean {
        return getHost(gameId) == playerId
    }

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId)?.host!!
    }

    fun setStatusForCurrentPlayerInGame(gameId: String, status: PlayerStatus) {
        launch {
            gameRepository.setStatusForPlayerInGame(gameId, currentUser.userId, status)
        }
    }

    fun setStatusForGame(gameId: String, status: GameStatus) {
        launch {
            gameRepository.setStatusForGame(gameId, status)
        }
    }
}
