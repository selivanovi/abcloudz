package com.example.spyfall.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.spyfall.data.entity.Role
import com.example.spyfall.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : BaseViewModel() {

    var isHost = false

    private val roleMutableChannel = Channel<Role>()
    val roleChannel = roleMutableChannel.receiveAsFlow()

    fun setRolesForPlayersInGame(gameId: String) {
        gameRepository.getPlayersFromGame(gameId).onEach {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { players ->
                        Log.d("RoleViewModel", players.toString())
                        players.random().role = Role.SPY
                        val role = Role.values().filter { it != Role.SPY }.random()
                        Log.d("RoleViewModel", role.toString())
                        roleMutableChannel.send(role)
                        players.forEach { player ->
                            if(player.role == null) {
                                player.role = role
                            }
                            launch {
                                gameRepository.addPlayerToGame(gameId, player)
                            }
                        }
                    }
                }
                it.isFailure -> {
                    it.exceptionOrNull()?.let { throwable ->
                        throw throwable
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

}