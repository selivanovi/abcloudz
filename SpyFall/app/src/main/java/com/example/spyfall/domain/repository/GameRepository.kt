package com.example.spyfall.domain.repository

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    val currentGame: GameDomain?
    val currentPlayer: PlayerDomain?

    suspend fun addGame(gameDomain: GameDomain)

    suspend fun getGame(gameId: String) : GameDomain

    fun observeGame() : Flow<Result<GameDomain>>

    suspend fun deleteGame()

    suspend fun addPlayerToGame(playerDomain: PlayerDomain)

    suspend fun updatePlayerInGame(playerDomain: PlayerDomain)

    suspend fun setStatusForCurrentPlayerInGame(status: PlayerStatus)

    fun observePlayersFromGame() : Flow<Result<List<PlayerDomain>>>

    fun observeCurrentPlayer() : Flow<Result<PlayerDomain>>

    suspend fun getPlayersFromGame() : List<PlayerDomain>

    suspend fun setTimeForGames(time: Int)

    suspend fun getDurationForGames(): Int


    suspend fun setStatusForGame(status: GameStatus)
}