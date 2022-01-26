package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.repository.GameRepository
import com.example.spyfall.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    fun createGame(): String {
        val player = Player(name = userRepository.getUserName()!!, status = null)
        val game = Game(
            host = player.name,
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