package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.repository.GameRepository
import com.example.spyfall.data.repository.UserRepository
import com.example.spyfall.utils.toPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    fun createGame(): String {
        val user = userRepository.getUser()!!
        val player = user.toPlayer()
        val game = Game(
            host = player.playerID,
            players = listOf(
                player
            ),
            status = GameStatus.CREATE
        )
        launch {
            gameRepository.addGame(game)
        }
        return game.gameID
    }
}