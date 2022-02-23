package com.example.spyfall.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.navigation.RoleDirections
import com.example.spyfall.ui.state.RoleState
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.DurationNotSetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    userRepository: UserRepository,
) : GameViewModel(gameRepository, userRepository) {

    private var isSpy: Boolean = false

    private var currentTime: Long? = null

    private var stopTimer: Boolean = false

    private val roleStateMutableChannel = Channel<RoleState>()
    val roleStateChannel = roleStateMutableChannel.receiveAsFlow()

    private var roleMutableChannel = Channel<Role>()
    val roleChannel = roleMutableChannel.receiveAsFlow()

    private var timeMutableChannel = Channel<Duration>()
    val timeChannel = timeMutableChannel.receiveAsFlow()

    private var timer: CountDownTimer? = null

    fun observeGame(gameId: String) {
        gameRepository.observeGame(gameId).onEach { game ->
            game?.duration?.let {
                timeMutableChannel.send(it.seconds)
            }
            when (game?.status) {
                GameStatus.PLAYING -> {
                    startTimer(gameId)
                    roleStateMutableChannel.send(RoleState.GameIsPlaying)
                }
                GameStatus.PAUSE -> {
                    stopTimer(gameId)
                    roleStateMutableChannel.send(RoleState.GameIsPause)
                }
                GameStatus.VOTE -> {
                    stopTimer(gameId)
                    if (isSpy) {
                        navigateToSpyVote(gameId)
                    } else {
                        navigateToLocationVote(gameId)
                    }
                }
                GameStatus.GAME_OVER -> {
                    stopTimer(gameId)
                    if (isSpy) {
                        navigateToSpyVote(gameId)
                    } else {
                        navigateToLocationVote(gameId)
                    }
                }
                GameStatus.LOCATION -> {
                    stopTimer(gameId)
                    if (isSpy) {
                        navigateToCallLocation(gameId)
                    } else {
                        navigateToCheckLocation(gameId)
                    }
                }
                else ->
                    Log.d("RoleViewModel", "Current state is: ${game?.status}")

            }

        }.launchIn(viewModelScope)
    }

    fun observeRoleOfCurrentPlayer(gameId: String) {
        gameRepository.observePlayerFromGame(gameId, currentUser.userId).onEach { player ->

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
        }.launchIn(viewModelScope)
    }

    fun setRolesInGame(gameId: String) {
        launch {
            if (isHost) return@launch
            val players = gameRepository.getPlayersFromGame(gameId)
            setRoleForPLayersInGame(gameId, players)
        }
    }

    fun setPauseOrPlayForGame(gameId: String) {
        launch {
            val game = gameRepository.getGame(gameId)
            val status =
                if (game?.status == GameStatus.PLAYING) GameStatus.PAUSE else GameStatus.PLAYING
            gameRepository.setStatusForGame(gameId, status)
        }
    }

    private fun stopTimer(gameId: String) {
        launch {
            stopTimer = true
            timer?.cancel()
            currentTime?.let {
                gameRepository.setDurationForGames(gameId, it)
            }
        }
    }

    private fun startTimer(gameId: String) {
        launch {
            gameRepository.getDurationForGames(gameId)
                .onSuccess { duration ->

                    stopTimer = false

                    if (duration < 0) {
                        currentTime = duration
                    } else {
                        startCountDownTimer(gameId, duration.seconds.inWholeMilliseconds)
                    }
                }
                .onFailure {
                    errorMutableChannel
                        .send(DurationNotSetException())
                }
        }
    }



    private fun startCountDownTimer(gameId: String, timeMillis: Long) {
        timer = object : CountDownTimer(timeMillis, Constants.TIMER_DELAY) {

            override fun onTick(time: Long) {
                launch {
                    timeMutableChannel.send(time.milliseconds)
                }
            }

            override fun onFinish() {
                launch {
                    gameRepository.setStatusForGame(gameId, GameStatus.GAME_OVER)
                }
            }
        }.start()

    }

    private suspend fun setRoleForPLayersInGame(gameId: String, players: List<PlayerDomain>) {
        if (players.all { it.role != null }) return

        players.random().role = Role.SPY

        val role = Role.values().filter { it != Role.SPY }.random()

        players.forEach { player ->
            if (player.role == null) {
                player.role = role
            }
            gameRepository.addPlayerToGame(gameId, player)
        }
    }

    private fun navigateToCheckLocation(gameId: String) {
        navigateTo(RoleDirections.toCheckLocationWithArgs(gameId))
    }

    private fun navigateToCallLocation(gameId: String) {
        navigateTo(RoleDirections.toCallLocationWithArgs(gameId))
    }

    private fun navigateToLocationVote(gameId: String) {
        navigateTo(RoleDirections.toLocationVoteWithArgs(gameId))
    }

    private fun navigateToSpyVote(gameId: String) {
        navigateTo(RoleDirections.toSpyVoteWithArgs(gameId))
    }
}
