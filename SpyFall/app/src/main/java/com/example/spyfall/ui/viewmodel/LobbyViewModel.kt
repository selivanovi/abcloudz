package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.utils.Constants
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.LobbyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private var playerStatus: PlayerStatus? = null
    private val currentPayer = userRepository.getUser()!!

    private var currentGame: GameDomain? = null

    private val lobbyStateMutableChannel = Channel<LobbyState>()
    val lobbyState = lobbyStateMutableChannel.receiveAsFlow()

    private val playersMutableChannel = Channel<List<PlayerDomain>>()
    val playersChannel = playersMutableChannel.receiveAsFlow()

    fun observePlayersFromGame(gameId: String) {
        gameRepository.observePlayersFromGame(gameId).onEach { result ->
            result.onSuccess { players ->

                playersMutableChannel.send(players)

                if (players.all { player -> player.status == PlayerStatus.PLAY }) {
                    if (players.size >= Constants.MIN_NUMBER_PLAYERS) {
                        lobbyStateMutableChannel.send(LobbyState.Play)
                        gameRepository.setStatusForGame(gameId, GameStatus.PLAYING)
                    } else lobbyStateMutableChannel.send(LobbyState.Wait)
                }

            }
            result.onFailure { throwable -> errorMutableChannel.send(throwable) }
        }.launchIn(viewModelScope)
    }

    fun setStatusPlayForPlayerInGame(gameId: String) {

        playerStatus = if (playerStatus == null) PlayerStatus.PLAY else null

        launch {
            gameRepository.setStatusForPlayerInGame(gameId, currentPayer.userId, playerStatus)
        }
    }
}





