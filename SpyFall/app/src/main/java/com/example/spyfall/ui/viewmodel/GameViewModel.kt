package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.state.GameState
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

open class GameViewModel(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val currentUser = userRepository.getUser()!!

    private val gameStatusMutableChannel = Channel<GameState>()

    fun observeGame(){

    }

    fun observePLayers() {

    }

    fun clearGame(gameId: String) {
        val isHost = async { checkHost(gameId, currentUser.userId) }
        launch {
            if (isHost.await()) {
                deleteGameById(gameId)
            } else {
                deletePlayerInGame(gameId, currentUser.userId)
            }
        }
    }

    private  suspend fun deletePlayerInGame(gameId: String, playerId: String) {
        gameRepository.deletePlayerInGame(gameId, playerId)
    }

    private suspend fun deleteGameById(gameId: String) {

        gameRepository.deleteGame(gameId)
    }

    private suspend fun checkHost(gameId: String, playerId: String): Boolean {
        return getHost(gameId) == playerId
    }

    private suspend fun getHost(gameId: String): String {
        return gameRepository.getGame(gameId)?.host!!
    }
}