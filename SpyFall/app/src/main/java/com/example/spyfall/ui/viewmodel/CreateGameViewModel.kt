package com.example.spyfall.ui.viewmodel


import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.entity.User
import com.example.spyfall.domain.repository.GameRepository
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
        launch {
            gameRepository.addGame(gameId, user.userId, GameStatus.CREATE)
            gameRepository.addPlayerToGame(gameId, user.toPlayerDomain())
        }
        this.gameId = gameId
        this.user = user
    }
}