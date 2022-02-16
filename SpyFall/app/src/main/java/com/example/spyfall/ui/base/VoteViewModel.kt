package com.example.spyfall.ui.base

import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.VoteState
import com.example.spyfall.ui.viewmodel.GameViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

abstract class VoteViewModel(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) : GameViewModel(gameRepository, userRepository) {

    private val voteStateMutableChannel = Channel<VoteState>()
    val voteStateChannel = voteStateMutableChannel.receiveAsFlow()

    fun observeVotePlayersInGame(gameId: String) {
        gameRepository.observePlayersFromGame(gameId).onEach { result ->
            result.onSuccess { players ->

                val spy = players.find { it.role == Role.SPY } ?: return@onEach
                val playersWithoutSpy = players.filter { it.role != Role.SPY }
                val currentPlayer =
                    players.find { it.playerId == currentUser.userId } ?: return@onEach

                if (currentPlayer.vote == null && currentPlayer.role != Role.SPY) {
                    voteStateMutableChannel.trySend(VoteState.WaitCurrentPlayer)
                } else {
                    if (playersWithoutSpy.all { it.vote != null }) {
                        if (playersWithoutSpy.all { player -> player.vote == spy.playerId }) {
                            navigateToLocationWonWithArgs(gameId)
                        } else {
                            if (gameRepository.getGame(gameId)?.status == GameStatus.GAME_OVER)
                                navigateToSpyWonWithArgs(gameId)
                            else {
                                gameRepository.setStatusForGame(gameId,GameStatus.PLAYING)
                                clearVoteForPlayersInGame(gameId, playersWithoutSpy)
                                navigateBack()
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

    private suspend fun clearVoteForPlayersInGame(gameId: String, players: List<PlayerDomain>) {
        players.forEach { player ->
            gameRepository.setVoteForPlayerInGame(gameId, player.playerId, null)
        }
    }

    abstract fun navigateToLocationWonWithArgs(gameId: String)

    abstract fun navigateToSpyWonWithArgs(gameId: String)
}


