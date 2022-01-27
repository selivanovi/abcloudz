package com.example.spyfall.data.repository

import android.util.Log
import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.utils.toPlayer
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
) : GameRepository {

    override suspend fun addGame(gameId: String, host: String, gameStatus: GameStatus) {

        val game = Game(host = host, status = gameStatus)

        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId).apply {
            setValue(game)
        }
    }

    override suspend fun addPlayerToGame(
        gameId: String,
        playerDomain: PlayerDomain
    ) {
        Log.d("GameRepository", playerDomain.toString())

        val player = playerDomain.toPlayer()

        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
            .child(PLAYERS_KEY_REFERENCES)
            .child(playerDomain.playerId).apply {
                setValue(player)
            }
    }


    override suspend fun setRolesForPlayersInGame(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGame() {
        TODO("Not yet implemented")
    }

    override fun observePlayersFromGame(gameId: String): Flow<Result<List<PlayerDomain>>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val players = snapshot.children.map {
                    val key = it.key
                    val player = it.getValue(Player::class.java)
                    if (key != null && player != null){
                        return@map PlayerDomain(key, player.name, player.status, player.role)
                    } else return@map null
                }
                this@callbackFlow.trySendBlocking(Result.success(players.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(GetDataException(Constants.GET_DATA_EXCEPTION)))
            }

        }

        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId).child(
            PLAYERS_KEY_REFERENCES
        ).addValueEventListener(
            valueEventListener
        )

        awaitClose {
            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId).child(
                PLAYERS_KEY_REFERENCES
            ).removeEventListener(valueEventListener)
        }
    }

    override fun getPlayersFromGame(gameId: String): Flow<Result<List<PlayerDomain>>>  = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val players = snapshot.children.map {
                    val key = it.key
                    val player = it.getValue(Player::class.java)
                    if (key != null && player != null){
                        return@map PlayerDomain(key, player.name, player.status, player.role)
                    } else return@map null
                }
                this@callbackFlow.trySendBlocking(Result.success(players.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(GetDataException(Constants.GET_DATA_EXCEPTION)))
            }

        }

        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId).child(
            PLAYERS_KEY_REFERENCES
        ).addListenerForSingleValueEvent(
            valueEventListener
        )

        awaitClose {
            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId).child(
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