package com.example.spyfall.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.RoleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private var isSpy: Boolean = false

    private val currentPlayer = userRepository.getUser()!!

    private val roleStateMutableChannel = Channel<RoleState>()
    val roleStateChannel = roleStateMutableChannel.receiveAsFlow()

    private val roleMutableChannel = Channel<Role>()
    val roleChannel = roleMutableChannel.receiveAsFlow()

    private val timeMutableChannel = Channel<Long?>()
    val timeChannel = timeMutableChannel.receiveAsFlow()

    fun observeGame(gameId: String) {
        gameRepository.observeGame(gameId).onEach { result ->
            result.onSuccess { game ->
                Log.d("RoleViewModel", "observeGame: $game")

                if (game?.status == GameStatus.VOTE) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.VoteSpy)
                    } else roleStateMutableChannel.send(RoleState.VotePlayer)
                }
                if (game?.status == GameStatus.GAME_OVER) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.VoteSpy)
                    } else roleStateMutableChannel.send(RoleState.VotePlayer)
                }
                if (game?.status == GameStatus.LOCATION) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.LocationSpy)
                    } else roleStateMutableChannel.send(RoleState.LocationPlayer)
                }
            }

            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }
        }.launchIn(viewModelScope)

    }

    fun observeRoleOfCurrentPlayer(gameId: String) {
        gameRepository.observePlayerFromGame(gameId, currentPlayer.userId).onEach { result ->
            result.onSuccess { player ->
                Log.d("RoleViewModel", "observeCurrentPlayer: $player")
                val role = player.role

                role?.let {
                    roleMutableChannel.send(it)
                }
                if (player.role == Role.SPY) {
                    isSpy = true
                    roleStateMutableChannel.send(RoleState.Spy)
                } else {
                    roleStateMutableChannel.send(RoleState.Player)
                }
                if (player.status == PlayerStatus.VOTED) {
                    roleStateMutableChannel.send(RoleState.Voted)
                }
            }
            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }
        }.launchIn(viewModelScope)
    }

    fun setRolesInGame(gameId: String) {

        val isHostDeferred = async { checkHost(gameId, currentPlayer.userId) }

        launch {
            val isHost = isHostDeferred.await()
            if (!isHost) return@launch
            val players = gameRepository.getPlayersFromGame(gameId)
            Log.d("RoleViewModel", "Get players from firebase")
            setRoleForPLayersInGame(gameId, players)
        }
    }

    fun setStatusForGame(gameId: String, status: GameStatus) {
        launch {
            gameRepository.setStatusForGame(gameId, status)
        }
    }


    private suspend fun setRoleForPLayersInGame(gameId: String, players: List<PlayerDomain>) {
        if (players.all { it.role != null }) return

        players.random().role = Role.SPY

        val role = Role.values().filter { it != Role.SPY }.random()

        players.forEach { player ->
            if (player.role == null) {
                player.role = role
            }
            gameRepository.updatePlayerInGame(gameId, player)
        }
    }

    fun setStatusForCurrentPlayerInGame(gameId: String, status: PlayerStatus) {
        launch {
            gameRepository.setStatusForPlayerInGame(gameId, currentPlayer.userId, status)
        }
    }


    private suspend fun checkHost(gameId: String, playerId: String): Boolean {
        return getHost(gameId) == playerId
    }

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId).host!!
    }

}

