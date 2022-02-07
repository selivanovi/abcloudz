package com.example.spyfall.ui.viewmodel


import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.utils.times
import com.example.spyfall.utils.toPlayerDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrepareGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val currentUser: UserDomain
        get() = userRepository.getUser()!!

    fun setGame(gameId: String) = launch {

        val game = gameRepository.getGame(gameId)

        if (game == null) createGame(gameId) else resetGame(gameId)
    }

    private suspend fun createGame(gameId: String) {
        val gameDomain = GameDomain(
            gameId = gameId,
            host = currentUser.userId,
            status = GameStatus.CREATE,
            duration = times.first()
        )

        val currentPLayer = currentUser.toPlayerDomain()

        gameRepository.addGame(gameDomain)
        gameRepository.addPlayerToGame(gameId, currentPLayer)
    }

    private suspend fun resetGame(gameId: String) {
        gameRepository.setStatusForGame(gameId, GameStatus.CREATE)
        gameRepository.setTimeForGames(gameId, times.first())

        val currentPlayer = currentUser.toPlayerDomain()
        gameRepository.addPlayerToGame(gameId, currentPlayer)
    }

    fun setTimeForGame(gameId: String, time: Int) {
        launch {
            gameRepository.setTimeForGames(gameId, time)
        }
    }

}