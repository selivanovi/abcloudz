package com.example.spyfall.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.viewModelScope
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

    var isHost = false

    private var countDownTimer: CountDownTimer? = null

    private var currentTime: Long = 0

    private val playersMutableChannel = Channel<List<PlayerDomain>>()
    val playersChannel = playersMutableChannel.receiveAsFlow()

    private val timeMutableChannel = Channel<Long?>()
    val timeChannel = timeMutableChannel.receiveAsFlow()

    fun observePLayersInGame(gameId: String) {
        gameRepository.getObservePlayersFromGame(gameId).onEach {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { players ->
                        playersMutableChannel.send(players)
                    }
                }
                it.isFailure -> {
                    it.exceptionOrNull()?.let { throwable ->
                        throw throwable
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setRolesInGame(gameId: String) {
        if (!isHost) return
        launch {
            val players = gameRepository.getPlayersFromGame(gameId)
            Log.d("RoleViewModel", "Get players from firebase")
            setRoleForPLayersInGame(gameId, players)
        }
    }

    fun setStatusForPLayerInGame(gameId: String, playerId: String, status: PlayerStatus) {
        launch {
            gameRepository.setStatusForPlayerInGame(gameId, playerId, status)
        }
    }

    fun pauseCountDownTimer(){
        countDownTimer?.cancel()
    }


    fun startCountDownTimer(gameId: String) {
        launch {
            var duration = gameRepository.getDurationForGames(gameId)

            duration *= 60000
            countDownTimer = object : CountDownTimer(duration.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    currentTime = millisUntilFinished
                    launch {
                        timeMutableChannel.send(millisUntilFinished)
                    }
                }

                override fun onFinish() {
                    launch {
                        timeMutableChannel.send(null)
                    }
                }

            }.apply { start() }

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
            gameRepository.addPlayerToGame(gameId, player)
        }
    }

}