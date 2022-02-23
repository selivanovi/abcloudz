package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.navigation.StartDirections
import com.example.spyfall.utils.GameIsPlayingException
import com.example.spyfall.utils.GameNotFoundException
import com.example.spyfall.utils.toPlayerDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    userRepository: UserRepository,
) : BaseViewModel() {

    val currentPLayer = userRepository.getUser() ?: throw IllegalArgumentException("User not found")

    fun joinToGame(gameId: String) = launch {
        val game = gameRepository.getGame(gameId)
        if (game == null) {
            errorMutableChannel.send(GameNotFoundException())
            return@launch
        } else if (game.status != GameStatus.CREATE) {
            errorMutableChannel.send(GameIsPlayingException())
            return@launch
        }

        val player = currentPLayer.toPlayerDomain()
        gameRepository.addPlayerToGame(gameId, player)
    }

    fun navigateToPrepareFragment(gameId: String) {
        navigateTo(StartDirections.toPrepareWithArgs(gameId))
    }

    fun navigateToWaitingGameFragment(gameId: String) {
        navigateTo(StartDirections.toWaitingGameWithArgs(gameId))
    }
}
