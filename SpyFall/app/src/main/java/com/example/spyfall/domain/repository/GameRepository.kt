package com.example.spyfall.domain.repository

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.PlayerDomain
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun addGame(gameId: String, host: String, gameStatus: GameStatus)

    suspend fun addPlayerToGame(gameId: String, playerDomain: PlayerDomain)

    suspend fun setRolesForPlayersInGame(gameId: String)

    suspend fun deleteGame()

    fun getObservePlayersFromGame(gameId: String) : Flow<Result<List<PlayerDomain>>>

    suspend fun getPlayersFromGame(gameId: String) : List<PlayerDomain>
}