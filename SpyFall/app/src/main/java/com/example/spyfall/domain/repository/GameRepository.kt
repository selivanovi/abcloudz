package com.example.spyfall.domain.repository

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun addGame(gameDomain: GameDomain)

    suspend fun getGame(gameId: String) : GameDomain?

    suspend fun setStatusForPlayerInGame(gameId: String, playerId: String, status: PlayerStatus?)

    suspend fun getDurationForGames(gameId: String): Int

    suspend fun setTimeForGames(gameId: String, time: Int)

    suspend fun getPlayersFromGame(gameId: String): List<PlayerDomain>

    fun observePlayerFromGame(gameId: String, playerId: String): Flow<Result<PlayerDomain>>

    fun observePlayersFromGame(gameId: String): Flow<Result<List<PlayerDomain>>>

    suspend fun deleteGame(gameId: String)

    suspend fun updatePlayerInGame(gameId: String, playerDomain: PlayerDomain)

    suspend fun addPlayerToGame(gameId: String, playerDomain: PlayerDomain)

    suspend fun deletePlayerInGame(gameId: String, playerId: String)

    fun observeGame(gameId: String): Flow<Result<GameDomain?>>

    suspend fun setStatusForGame(gameId: String, status: GameStatus)

    suspend fun setVoteForPlayerInGame(gameId: String, playerId: String, vote: PlayerDomain?)
}