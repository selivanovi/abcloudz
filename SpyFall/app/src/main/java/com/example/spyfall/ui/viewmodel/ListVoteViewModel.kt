package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListVoteViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    var playerForVote: PlayerDomain? = null

    private val currentPlayer = userRepository.getUser()!!

    private val playersMutableChannel = Channel<List<PlayerDomain>>()
    val playersChannel = playersMutableChannel.receiveAsFlow()

    fun observePlayersFromGame(gameId: String) {
        launch {
            gameRepository.observePlayersFromGame(gameId).collect {
                it.onSuccess { players ->

                    val playersWithoutCurrent = players.filter { player -> player.playerId != currentPlayer.userId }
                    playersMutableChannel.send(playersWithoutCurrent)
                }
                it.onFailure { throwable -> errorMutableChannel.send(throwable) }
            }
        }
    }

    fun sendVoteForCurrentPLayerInGame(gameId: String) {
        playerForVote?.let { player ->
            launch {
                gameRepository.setVoteForPlayerInGame(gameId, currentPlayer.userId, player)
            }
        }
    }
}
