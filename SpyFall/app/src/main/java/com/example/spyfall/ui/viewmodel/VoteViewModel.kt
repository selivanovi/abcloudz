package com.example.spyfall.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {


    private val playerMutableChannel = Channel<List<PlayerDomain>>()
    val playerChannel = playerMutableChannel.receiveAsFlow()
    private val resultMutableVoteChannel = Channel<Role?>()
    val resultVoteChannel = resultMutableVoteChannel.receiveAsFlow()

    fun observeVotePlayersInGame(gameId: String, isFinished: Boolean) {
        gameRepository.getObservePlayersFromGame(gameId).onEach {
            Log.d("VoteViewModel", "observe $it")
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { players ->
                        Log.d("VoteViewModel", "$players")
                        var spy: PlayerDomain? = null
                        players.forEach {
                            if(it.role == Role.SPY) spy = it
                        }
                        val playersWithoutSpy =
                            players.filter { playerDomain -> playerDomain.role != Role.SPY }

                        spy?.let { spyNotNull ->
                            if (playersWithoutSpy.all { it.vote != null }){
                                if(playersWithoutSpy.all { it.vote == spyNotNull.name }) {
                                    launch {
                                        resultMutableVoteChannel.send(playersWithoutSpy.first().role)
                                    }
                                }
                                else {
                                    launch {
                                        if (isFinished) resultMutableVoteChannel.send(Role.SPY)
                                        else resultMutableVoteChannel.send(null)
                                    }
                                }
                            }
                        }
                        playerMutableChannel.send(players)
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