package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val currentPlayer = userRepository.getUser()!!


    private val voteStateMutableChannel = Channel<VoteState>()
    val voteStateChannel = voteStateMutableChannel.receiveAsFlow()

    fun observeVotePlayersInGame(gameId: String) {
        gameRepository.observePlayersFromGame(gameId).onEach { result ->
            result.onSuccess { players ->

                val spy = players.find { it.role == Role.SPY } ?: return@onEach
                val playersWithoutSpy = players.filter { it.role != Role.SPY }
                val currentPlayer = players.find { it.playerId == currentPlayer.userId } ?: return@onEach

                if (currentPlayer.vote == null) {
                    voteStateMutableChannel.trySend(VoteState.WaitCurrentPlayerState)
                } else {
                    if (playersWithoutSpy.all { it.vote != null }) {
                        if (playersWithoutSpy.all { player -> player.vote == spy.playerId }) {
                            voteStateMutableChannel.trySend(VoteState.SpyLostState)
                        } else {
                            if (gameRepository.getGame(gameId).status == GameStatus.GAME_OVER)
                                voteStateMutableChannel.send(VoteState.SpyWonState)
                            else {
                                voteStateMutableChannel.send(VoteState.GameContinueState)
                                gameRepository.setStatusForGame(gameId, GameStatus.PLAYING)
                            }
                        }
                    } else {
                        voteStateMutableChannel.trySend(VoteState.WaitOtherPlayersState)
                    }
                }
            }
            result.onFailure { throw it }
        }.launchIn(viewModelScope)
    }
}

sealed class VoteState {

    object WaitCurrentPlayerState : VoteState()
    object WaitOtherPlayersState : VoteState()
    object SpyLostState : VoteState()
    object SpyWonState : VoteState()
    object GameContinueState : VoteState()
}
