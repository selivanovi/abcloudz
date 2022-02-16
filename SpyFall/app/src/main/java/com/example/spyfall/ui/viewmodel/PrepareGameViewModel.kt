package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.navigation.PrepareGameDirections
import com.example.spyfall.utils.times
import com.example.spyfall.utils.toPlayerDomain
import com.example.spyfall.utils.toSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrepareGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val prepareGameDirections: PrepareGameDirections
) : GameViewModel(gameRepository, userRepository) {

    fun setupGame(gameId: String) {
        launch {
            val game = gameRepository.getGame(gameId)

            if (game == null) createGame(gameId) else resetGame(gameId)
        }
    }

    private suspend fun createGame(gameId: String) {
        val gameDomain = GameDomain(
            gameId = gameId,
            host = currentUser.userId,
            status = GameStatus.CREATE,
            duration = times.first().toSeconds()
        )

        val currentPLayer = currentUser.toPlayerDomain()

        gameRepository.addGame(gameDomain)
        gameRepository.addPlayerToGame(gameId, currentPLayer)
    }

    private suspend fun resetGame(gameId: String) {
        gameRepository.setStatusForGame(gameId, GameStatus.CREATE)
        gameRepository.setDurationForGames(gameId, times.first().toSeconds())

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
