package com.example.spyfall.data.repository

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun addGame(game: Game)

    suspend fun addPlayerToGame(gameID: String, player: Player)

    suspend fun deleteGame()

    fun getCurrentGameId(): String?

    fun getPlayersFromGame(gameID: String) : Flow<Result<List<Player>>>
}