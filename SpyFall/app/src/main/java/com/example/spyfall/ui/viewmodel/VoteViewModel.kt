package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.VoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
                val currentPlayer =
                    players.find { it.playerId == currentPlayer.userId } ?: return@onEach

                if (currentPlayer.vote == null && currentPlayer.role != Role.SPY) {
                    voteStateMutableChannel.trySend(VoteState.WaitCurrentPlayer)
                } else {
                    if (playersWithoutSpy.all { it.vote != null }) {
                        if (playersWithoutSpy.all { player -> player.vote == spy.playerId }) {
                            voteStateMutableChannel.trySend(VoteState.SpyLost)
                        } else {
                            if (gameRepository.getGame(gameId).status == GameStatus.GAME_OVER)
                                voteStateMutableChannel.send(VoteState.SpyWon)
                            else {
                                clearVoteForPlayersInGame(gameId, playersWithoutSpy)
                                voteStateMutableChannel.send(VoteState.GameContinue)
                            }
                        }
                    } else {
                        voteStateMutableChannel.trySend(VoteState.WaitOtherPlayers)
                    }
                }
            }
            result.onFailure { throw it }
        }.launchIn(viewModelScope)
    }

    private fun clearVoteForPlayersInGame(gameId: String, players: List<PlayerDomain>) {
        players.forEach { player ->
            launch {
                gameRepository.setVoteForPlayerInGame(gameId, player.playerId, null)
            }
        }
    }
}


