package com.example.spyfall.data.repository

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : GameRepository {

    override suspend fun addGame(game: Game) {
        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(game.gameID).apply {
            child("host").setValue(game.host)
            child("status").setValue(game.status)
            game.players?.forEach { player ->
                addPlayerToGame(gameID = game.gameID, player = player)
            }
        }
    }

    override suspend fun addPlayerToGame(gameID: String, player: Player) {
        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameID)
            .child(PLAYERS_KEY_REFERENCES)
            .child(player.playerID).apply {
                child("name").setValue(player.name)
                child("status").setValue(player.status)
            }
    }

    override suspend fun deleteGame() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val GAMES_KEY_REFERENCES = "games"
        private const val PLAYERS_KEY_REFERENCES = "players"
    }
}