package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    fun setTimeForGame(time: Int) {
        launch {
            gameRepository.setTimeForGames(time)
        }
    }
}