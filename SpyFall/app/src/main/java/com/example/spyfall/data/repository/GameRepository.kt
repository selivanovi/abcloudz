package com.example.spyfall.data.repository

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player

interface GameRepository {

    suspend fun addGame(game: Game)

    suspend fun addPlayerToGame(gameID: String, player: Player)

    suspend fun deleteGame()
}