package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ListVoteViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel(){

    private val playersMutableChannel = Channel<List<PlayerDomain>>()
    val playersChannel = playersMutableChannel.receiveAsFlow()

    fun observePlayersFromCurrentGame() {
        gameRepository.observePlayersFromGame().onEach {
            it.onSuccess { players ->
                playersMutableChannel.send(players)
            }
            it.onFailure { throwable -> errorMutableChannel.send(throwable) }
        }.launchIn(viewModelScope)
    }
}