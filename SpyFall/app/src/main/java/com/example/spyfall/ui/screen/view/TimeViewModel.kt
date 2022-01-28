package com.example.spyfall.ui.screen.view

import com.example.spyfall.data.repository.GameRepositoryImpl
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    fun setTimeForGame(gameId: String, time: Int) {
        launch {
            gameRepository.setTimeForGames(gameId, time)
        }
    }
}