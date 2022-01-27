package com.example.spyfall.ui.viewmodel

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.repository.GameRepository
import com.example.spyfall.data.repository.UserRepository
import com.example.spyfall.utils.generateRandomID
import com.example.spyfall.utils.toPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor() : BaseViewModel() {

    fun generateGameId(): String {
        return generateRandomID()
    }
}