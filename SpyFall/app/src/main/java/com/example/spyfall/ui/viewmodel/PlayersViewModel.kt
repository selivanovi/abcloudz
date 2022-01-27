package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    private val playersMutableChannel = Channel<List<Player>>()
    val playersChannel = playersMutableChannel.receiveAsFlow()

    fun observePlayers() {

        gameRepository.getPlayersFromGame("")
    }
}