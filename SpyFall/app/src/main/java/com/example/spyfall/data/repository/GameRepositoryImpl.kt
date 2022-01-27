package com.example.spyfall.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.example.spyfall.data.utils.InvalidNameException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences
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

    override fun getCurrentGameId(): String? =
        sharedPreferences.getString(KEY_CURRENT_GAME, null)


    override fun getPlayersFromGame(gameID: String): Flow<Result<List<Player>>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val players = snapshot.children.map { it.getValue(Player::class.java) }
                this@callbackFlow.trySendBlocking(Result.success(players.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(GetDataException(Constants.GET_DATA_EXCEPTION)))
            }

        }

        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameID).child(
            PLAYERS_KEY_REFERENCES
        ).addListenerForSingleValueEvent(
            valueEventListener
        )

        awaitClose {
            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameID).child(
                PLAYERS_KEY_REFERENCES
            ).removeEventListener(valueEventListener)
        }
    }

    companion object {
        private const val KEY_CURRENT_GAME = "key_current_game"
        private const val GAMES_KEY_REFERENCES = "games"
        private const val PLAYERS_KEY_REFERENCES = "players"
    }
}