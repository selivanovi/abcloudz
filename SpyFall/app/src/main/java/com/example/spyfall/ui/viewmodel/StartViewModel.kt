package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.GameIsPlayingException
import com.example.spyfall.utils.GameNotFoundException
import com.example.spyfall.utils.toPlayerDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val currentPLayer = userRepository.getUser()!!

    fun joinToGame(gameId:String) = launch {
        val game = gameRepository.getGame(gameId)
        if (game == null) {
            errorMutableChannel.send(GameNotFoundException(Constants.GAME_NOT_FOUND_EXCEPTION))
            return@launch
        }
        else if (game.status != GameStatus.CREATE) {
            errorMutableChannel.send(GameIsPlayingException(Constants.GAME_IS_PLAYING_EXCEPTION))
            return@launch
        }

        val player = currentPLayer.toPlayerDomain()
        gameRepository.addPlayerToGame(gameId, player)
    }

}