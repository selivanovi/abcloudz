package com.example.spyfall.domain.repository

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun addGame(gameId: String, host: String, gameStatus: GameStatus)

    suspend fun getGame(gameId: String) : GameDomain

    suspend fun addPlayerToGame(gameId: String, playerDomain: PlayerDomain)

    suspend fun setRolesForPlayersInGame(gameId: String)

    suspend fun deleteGame()

    fun getObservePlayersFromGame(gameId: String) : Flow<Result<List<PlayerDomain>>>

    suspend fun getPlayersFromGame(gameId: String) : List<PlayerDomain>

    suspend fun setTimeForGames(gameId: String, time: Int)
}