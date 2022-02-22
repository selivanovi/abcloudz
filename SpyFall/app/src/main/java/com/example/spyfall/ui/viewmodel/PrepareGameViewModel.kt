package com.example.spyfall.ui.viewmodel

import android.util.Log
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.navigation.PrepareGameDirections
import com.example.spyfall.ui.state.PrepareState
import com.example.spyfall.utils.times
import com.example.spyfall.utils.toPlayerDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrepareGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    userRepository: UserRepository,
    private val prepareGameDirections: PrepareGameDirections
) : GameViewModel(gameRepository, userRepository) {

    private val prepareMutableStateChannel = Channel<PrepareState>()
    val prepareStateChannel = prepareMutableStateChannel.receiveAsFlow()

    fun setupGame(gameId: String) {
        launch {
            val game = gameRepository.getGame(gameId)
            Log.d("GameViewMode", "game: $game")
            if (game == null)
                createGame(gameId)
            else
                resetGame(gameId)
            prepareMutableStateChannel.send(PrepareState.GameIsReady)
        }
    }


    private suspend fun createGame(gameId: String) {
        val gameDomain = GameDomain(
            gameId = gameId,
            host = currentUser.userId,
            status = GameStatus.CREATE,
            duration = times.first().inWholeSeconds
        )

        val currentPLayer = currentUser.toPlayerDomain()

        gameRepository.addGame(gameDomain)
        gameRepository.addPlayerToGame(gameId, currentPLayer)
    }

    private suspend fun resetGame(gameId: String) {
        gameRepository.setStatusForGame(gameId, GameStatus.CREATE)
        gameRepository.setDurationForGames(gameId, times.first().inWholeSeconds)

        val currentPlayer = currentUser.toPlayerDomain()
        gameRepository.addPlayerToGame(gameId, currentPlayer)
    }

    fun setDuration(gameId: String, time: Long) = launch {
        setDurationForGame(gameId, time)
    }

    fun navigateToRoleWithArgs(gameId: String) {
        navigateTo(prepareGameDirections.toRoleWithArgs(gameId))
    }


}
