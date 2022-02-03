package com.example.spyfall.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
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


    private val timeMutableChannel = Channel<Long?>()
    val timeChannel = timeMutableChannel.receiveAsFlow()

    fun observeGame(gameId: String) {
        gameRepository.observeGame(gameId).onEach { result ->
            result.onSuccess { game ->
                Log.d("RoleViewModel", "observeGame: $game")
                if (game.status == GameStatus.VOTE) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.VoteSpyState)
                    } else roleStateMutableChannel.send(RoleState.VotePlayerState)
                }
                if (game.status == GameStatus.GAME_OVER) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.VoteSpyState)
                    } else roleStateMutableChannel.send(RoleState.VotePlayerState)
                }
                if (game.status == GameStatus.LOCATION) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.LocationSpyState)
                    } else roleStateMutableChannel.send(RoleState.LocationPlayerState)
                }
            }
            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }
        }.launchIn(viewModelScope)

    }

    fun observeRoleOfCurrentPlayer(gameId: String) {
        gameRepository.observePlayerInGame(gameId, currentPlayer.userId).onEach { result ->
            result.onSuccess { player ->
                Log.d("RoleViewModel", "observeCurrentPlayer: $player")
                val role = player.role

                if (role != null) {
                    roleStateMutableChannel.send(RoleState.SetRoleState(role = role))
                }
                if (player.role == Role.SPY) {
                    isSpy = true
                    roleStateMutableChannel.send(RoleState.SpyState)
                } else {
                    roleStateMutableChannel.send(RoleState.PlayerState)
                }
                if (player.status == PlayerStatus.VOTED) {
                    roleStateMutableChannel.send(RoleState.VotedState)
                }
            }
            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }
        }.launchIn(viewModelScope)
    }

    fun setRolesInGame(gameId: String) {

        val isHost = async { getHost(gameId) == currentPlayer.userId }

        launch {
            if (isHost.await()) return@launch
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

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId).gameId
    }

}

sealed class RoleState {

    object VotedState : RoleState()
    object VoteSpyState : RoleState()
    object VotePlayerState : RoleState()
    object LocationSpyState : RoleState()
    object LocationPlayerState : RoleState()
    object SpyState : RoleState()
    object PlayerState : RoleState()
    class SetRoleState(val role: Role) : RoleState()
}