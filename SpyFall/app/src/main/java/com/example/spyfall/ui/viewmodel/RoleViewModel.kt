package com.example.spyfall.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    private val isHost: Boolean
        get() = gameRepository.currentGame == null

    private val isSpy: Boolean
        get() = gameRepository.currentPlayer?.role == Role.SPY

    private val roleStateMutableChannel = Channel<RoleState>()
    val roleStateChannel = roleStateMutableChannel.receiveAsFlow()


    private val timeMutableChannel = Channel<Long?>()
    val timeChannel = timeMutableChannel.receiveAsFlow()

    fun observeGame() {
        gameRepository.observeGame().onEach { result ->
            result.onSuccess { game ->
                if (game.status == GameStatus.VOTE || game.status == GameStatus.GAME_OVER) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.VoteSpyState)
                    } else roleStateMutableChannel.send(RoleState.VotePlayerState)
                }
                if (game.status == GameStatus.LOCATION) {
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.LocationPlayerState)
                    } else roleStateMutableChannel.send(RoleState.LocationSpyState)
                }
            }
            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }

        }.launchIn(viewModelScope)
    }

    fun observeRoleOfCurrentPlayer() {
        gameRepository.observeCurrentPlayer().onEach { result ->
            result.onSuccess { player ->

                val role = player.role

                if (role != null) {
                    roleStateMutableChannel.send(RoleState.SetRoleState(role = role))
                }
            }
            result.onFailure { throwable ->
                errorMutableChannel.send(throwable)
            }

        }.launchIn(viewModelScope)
    }

    fun setRolesInGame() {
        if (!isHost) return
        launch {
            val players = gameRepository.getPlayersFromGame()
            Log.d("RoleViewModel", "Get players from firebase")
            setRoleForPLayersInGame(players)
        }
    }

    fun setStatusForСurrentPlayerInGame(status: PlayerStatus) {
        launch {
            gameRepository.setStatusForCurrentPlayerInGame(status)
        }
    }

    private suspend fun setRoleForPLayersInGame(players: List<PlayerDomain>) {
        if (players.all { it.role != null }) return

        players.random().role = Role.SPY

        val role = Role.values().filter { it != Role.SPY }.random()

        players.forEach { player ->
            if (player.role == null) {
                player.role = role
            }
            gameRepository.updatePlayerInGame(player)
        }
    }

}

sealed class RoleState {

    object VoteSpyState : RoleState()
    object VotePlayerState : RoleState()
    object LocationSpyState : RoleState()
    object LocationPlayerState : RoleState()
    class SetRoleState(val role: Role) : RoleState()
}