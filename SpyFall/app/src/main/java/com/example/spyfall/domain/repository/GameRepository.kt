package com.example.spyfall.domain.repository

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun addGame(gameDomain: GameDomain)

    suspend fun getGame(gameId: String): GameDomain?

    suspend fun setStatusForPlayerInGame(gameId: String, playerId: String, status: PlayerStatus?)

    suspend fun getDurationForGames(gameId: String): Result<Long>

    suspend fun setDurationForGames(gameId: String, time: Long)

    suspend fun getPlayersFromGame(gameId: String): List<PlayerDomain>

    fun observePlayerFromGame(gameId: String, playerId: String): Flow<PlayerDomain>

    fun observePlayersFromGame(gameId: String): Flow<List<PlayerDomain>>

    suspend fun deleteGame(gameId: String)

    suspend fun addPlayerToGame(gameId: String, playerDomain: PlayerDomain)

    suspend fun deletePlayerInGame(gameId: String, playerId: String)

    fun observeGame(gameId: String): Flow<GameDomain?>

    suspend fun setStatusForGame(gameId: String, status: GameStatus)

    suspend fun setVoteForPlayerInGame(gameId: String, playerId: String, vote: PlayerDomain?)
}
