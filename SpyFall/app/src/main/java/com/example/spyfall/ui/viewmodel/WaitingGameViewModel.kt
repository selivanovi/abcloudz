package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val currentUser = userRepository.getUser()!!

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
