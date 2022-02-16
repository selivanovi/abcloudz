package com.example.spyfall.data.repository

import android.util.Log
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.GetDataException
import com.example.spyfall.utils.toGame
import com.example.spyfall.utils.toGameDomain
import com.example.spyfall.utils.toPlayer
import com.example.spyfall.utils.toPlayerDomain
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : GameRepository {

    private val gameReferences: (gameId: String) -> DatabaseReference = { gameId ->
        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
    }

    private val playerReferences: (gameId: String, playerId: String) -> DatabaseReference =
        { gameId, playerId ->
            gameReferences(gameId).child(PLAYERS_KEY_REFERENCES).child(playerId)
        }

    override suspend fun addGame(gameDomain: GameDomain) {
        gameReferences(gameDomain.gameId).setValue(gameDomain.toGame())
    }

    override suspend fun getGame(gameId: String): GameDomain? =
        suspendCancellableCoroutine { continuation ->
            val db = gameReferences(gameId)

            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(GetDataException(Constants.GET_DATA_EXCEPTION))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val game = snapshot.toGameDomain()
                        continuation.resume(game)
                    } catch (exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
            continuation.invokeOnCancellation { db.removeEventListener(listener) }
            db.addListenerForSingleValueEvent(listener)
        }

    override fun observeGame(gameId: String): Flow<Result<GameDomain?>> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.toGameDomain()
                    this@callbackFlow.trySendBlocking(Result.success(game))
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(
                        Result.failure(
                            GetDataException(Constants.GET_DATA_EXCEPTION)
                        )
                    )
                }
            }

            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
                .addValueEventListener(
                    valueEventListener
                )

            awaitClose {
                firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
                    .removeEventListener(valueEventListener)
            }
        }

    override suspend fun addPlayerToGame(
        gameId: String,
        playerDomain: PlayerDomain
    ) {
        val player = playerDomain.toPlayer()

        playerReferences(gameId, playerDomain.playerId.toString()).setValue(player)
    }

    override suspend fun deletePlayerInGame(gameId: String, playerId: String) {
        playerReferences(gameId, playerId).setValue(null)
    }

    override suspend fun deleteGame(gameId: String) {
        gameReferences(gameId).setValue(null)
    }

    override fun observePlayersFromGame(gameId: String): Flow<Result<List<PlayerDomain>>> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val players = snapshot.children.map {
                        it.toPlayerDomain()
                    }
                    this@callbackFlow.trySendBlocking(Result.success(players.filterNotNull()))
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(
                        Result.failure(
                            GetDataException(Constants.GET_DATA_EXCEPTION)
                        )
                    )
                }
            }

            Log.d("GameRepository", "observePlayersFromGame: $gameId")

            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
                .child(
                    PLAYERS_KEY_REFERENCES
                ).addValueEventListener(
                    valueEventListener
                )

            awaitClose {
                firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
                    .child(
                        PLAYERS_KEY_REFERENCES
                    ).removeEventListener(valueEventListener)
            }
        }

    override fun observePlayerFromGame(
        gameId: String,
        playerId: String
    ): Flow<Result<PlayerDomain>> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val player = snapshot.toPlayerDomain()
                    player?.let {
                        this@callbackFlow.trySendBlocking(Result.success(it))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(
                        Result.failure(
                            GetDataException(Constants.GET_DATA_EXCEPTION)
                        )
                    )
                }
            }

            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
                .child(
                    PLAYERS_KEY_REFERENCES
                ).child(playerId).addValueEventListener(
                    valueEventListener
                )

            awaitClose {
                firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
                    .child(
                        PLAYERS_KEY_REFERENCES
                    ).child(playerId).removeEventListener(valueEventListener)
            }
        }

    override suspend fun getPlayersFromGame(gameId: String): List<PlayerDomain> {
        Log.d("GamerRepository", "getPlayerFromGame")
        val db = gameReferences(gameId).child(PLAYERS_KEY_REFERENCES)
        return db.get().await().children.mapNotNull { it.toPlayerDomain() }
    }

    override suspend fun setDurationForGames(gameId: String, time: Long) {
        firebaseDatabase.reference
            .child(GAMES_KEY_REFERENCES)
            .child(gameId)
            .child(DURATION_KEY_REFERENCES).setValue(time)
    }

    override suspend fun getDurationForGames(gameId: String): Long {
        return gameReferences(gameId).get().await().child(DURATION_KEY_REFERENCES)
            .getValue(Long::class.java)!!
    }

    override suspend fun setStatusForPlayerInGame(
        gameId: String,
        playerId: String,
        status: PlayerStatus?
    ) {
        playerReferences(
            gameId,
            playerId
        ).child(STATUS_KEY_REFERENCES).setValue(status)
    }

    override suspend fun setStatusForGame(gameId: String, status: GameStatus) {
        gameReferences(gameId).child(STATUS_KEY_REFERENCES).setValue(status)
    }

    override suspend fun setVoteForPlayerInGame(
        gameId: String,
        playerId: String,
        vote: PlayerDomain?
    ) {
        playerReferences(gameId, playerId).child(VOTE_KEY_REFERENCES)
            .setValue(vote?.playerId)
    }

    companion object {
        private const val GAMES_KEY_REFERENCES = "games"
        private const val PLAYERS_KEY_REFERENCES = "players"
        private const val DURATION_KEY_REFERENCES = "duration"
        private const val STATUS_KEY_REFERENCES = "status"
        private const val VOTE_KEY_REFERENCES = "vote"
    }
}
