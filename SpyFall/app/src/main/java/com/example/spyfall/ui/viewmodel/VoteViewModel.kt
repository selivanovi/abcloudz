package com.example.spyfall.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    private val playerMutableChannel = Channel<List<PlayerDomain>>()
    // val playerChannel = playerMutableChannel.receiveAsFlow()

    val playersChannel = Channel<List<PlayerDomain>>()

    private val resultMutableVoteChannel = Channel<Role?>()
    val resultVoteChannel = resultMutableVoteChannel.receiveAsFlow()

    val voteStateChannel = Channel<VoteState>()

    val currentPlayerId = "1"

    fun observeVotePlayersInGameNew(gameId: String, isFinished: Boolean) {
        gameRepository.getObservePlayersFromGame(gameId).onEach { result ->
            result.onSuccess { players ->
                playersChannel.trySend(players)
                val spy = players.find { it.role == Role.SPY } ?: return@onEach
                val playersWithoutSpy = players.filter { it.role != Role.SPY }
                val currentPlayer = players.find { it.playerId == currentPlayerId } ?: return@onEach

                if (currentPlayer.vote == null) {
                    voteStateChannel.trySend(WaitCurrentPlayerState)
                } else {
                    if (playersWithoutSpy.all { it.vote != null }) {
                        if (playersWithoutSpy.all { player -> player.vote == spy.name }) {
                            voteStateChannel.trySend(SpyLostState)
                        } else {
                            if (isFinished) voteStateChannel.send(SpyWonState)
                            else voteStateChannel.send(GameContinueState)
                        }
                    } else {
                        voteStateChannel.trySend(WaitOtherPlayersState)
                    }
                }
            }
            result.onFailure { throw it }
        }.launchIn(viewModelScope)
    }

    fun observeVotePlayersInGame(gameId: String, isFinished: Boolean) {
        gameRepository.getObservePlayersFromGame(gameId).onEach {
            Log.d("VoteViewModel", "observe ${it.getOrNull()}")
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { players ->

                        playersChannel.trySend(players)

                        Log.d("VoteViewModel", "$players")
                        var spy: PlayerDomain? = null
                        players.forEach { player ->
                            spy = if (player.role == Role.SPY) player else null
                        }

                        val playersWithoutSpy =
                            players.filter { playerDomain -> playerDomain.role != Role.SPY }

                        spy?.let { spyNotNull ->
                            if (playersWithoutSpy.all { player -> player.vote != null }) {
                                if (playersWithoutSpy.all { player -> player.vote == spyNotNull.name }) {
                                    resultMutableVoteChannel.send(playersWithoutSpy.first().role)
                                } else {
                                    if (isFinished) resultMutableVoteChannel.send(Role.SPY)
                                    else resultMutableVoteChannel.send(null)
                                }
                            }
                        }
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
}

sealed class VoteState

object WaitCurrentPlayerState : VoteState()
object WaitOtherPlayersState : VoteState()
object SpyLostState : VoteState()
object SpyWonState : VoteState()
object GameContinueState : VoteState()
