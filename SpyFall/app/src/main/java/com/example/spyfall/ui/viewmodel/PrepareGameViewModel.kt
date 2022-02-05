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

    var gameId: String? = null
    var userDomain: UserDomain? = null

    fun createGame(gameId: String, userDomain: UserDomain) {

        val gameDomain = GameDomain(
            gameId = gameId,
            host = userDomain.userId,
            status = GameStatus.CREATE,
            duration = times.first()
        )

        launch {
            gameRepository.addGame(gameDomain)
            gameRepository.addPlayerToGame(gameId, userDomain.toPlayerDomain())
        }
        this.gameId = gameId
        this.userDomain = userDomain
    }

    fun setTimeForGame(gameId: String, time: Int) {
        launch {
            gameRepository.setTimeForGames(gameId, time)
        }
    }

    fun getUser(): UserDomain? =
        userRepository.getUser()

}