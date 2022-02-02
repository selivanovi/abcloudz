package com.example.spyfall.ui.viewmodel


import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.User
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.utils.times
import com.example.spyfall.utils.toPlayerDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    var gameId: String? = null
    var user: User? = null

    fun createGame(gameId: String, user: User) {

        val gameDomain = GameDomain(
            gameId = gameId,
            host = user.userId,
            status = GameStatus.CREATE,
            duration = times.first()
        )

        launch {
            gameRepository.addGame(gameDomain)
            gameRepository.addPlayerToGame(gameId, user.toPlayerDomain())
        }
        this.gameId = gameId
        this.user = user
    }
}