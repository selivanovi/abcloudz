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

    private var currentTime: Long? = null

    private var stopTimer: Boolean = false

    private val currentPlayer = userRepository.getUser()!!

    private val roleStateMutableChannel = Channel<RoleState>()
    val roleStateChannel = roleStateMutableChannel.receiveAsFlow()

    private val roleMutableChannel = Channel<Role>()
    val roleChannel = roleMutableChannel.receiveAsFlow()

    private val timeMutableChannel = Channel<Long>()
    val timeChannel = timeMutableChannel.receiveAsFlow()

    fun observeGame(gameId: String) {
        gameRepository.observeGame(gameId).onEach { result ->
            result.onSuccess { game ->
                Log.d("RoleViewModel", "observeGame: $game")

                if (game?.status == GameStatus.PLAYING) {
                    startTimer(gameId)
                    roleStateMutableChannel.send(RoleState.GameIsPlaying)
                }
                if (game?.status == GameStatus.PAUSE) {
                    stopTimer(gameId)
                    roleStateMutableChannel.send(RoleState.GameIsPause)
                }
                if (game?.status == GameStatus.VOTE) {
                    stopTimer(gameId)
                    if (isSpy) {
                        roleStateMutableChannel.send(RoleState.VoteSpy)
                    } else roleStateMutableChannel.send(RoleState.VotePlayer)
                }
                if (game?.status == GameStatus.GAME_OVER) {
                    stopTimer(gameId)
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

    private fun stopTimer(gameId: String) = launch {
        stopTimer = true
        currentTime?.let {
            gameRepository.setDurationForGames(gameId, it)
        }
    }


    private fun startTimer(gameId: String) = launch {

        stopTimer = false

        val duration = gameRepository.getDurationForGames(gameId)

        if (duration < 0) currentTime = duration
        else {
            for (i in duration downTo 0) {
                if (stopTimer) {
                    return@launch
                }
                currentTime = i
                timeMutableChannel.send(i)
                delay(1000)
            }

            gameRepository.setStatusForGame(gameId, GameStatus.GAME_OVER)
        }
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

    fun setPauseOrPlayForGame(gameId: String) = launch {
        val game = gameRepository.getGame(gameId)
        val status =
            if (game?.status == GameStatus.PLAYING) GameStatus.PAUSE else GameStatus.PLAYING
        gameRepository.setStatusForGame(gameId, status)
    }


    private suspend fun checkHost(gameId: String, playerId: String): Boolean {
        return getHost(gameId) == playerId
    }

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId)?.host!!
    }

}

