package com.example.spyfall.ui.viewmodel

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
) : BaseViewModel(){


    var players: List<PlayerDomain>? = null

    private val resultMutableVoteChannel = Channel<List<PlayerDomain>>()
    val resultVoteChannel = resultMutableVoteChannel.receiveAsFlow()

    fun observePLayersInGame(gameId: String) {
        gameRepository.getObservePlayersFromGame(gameId).onEach {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { players ->
                        this.players = players
                        val playersWithoutSpy = players.filter { playerDomain ->  playerDomain.role != Role.SPY}
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