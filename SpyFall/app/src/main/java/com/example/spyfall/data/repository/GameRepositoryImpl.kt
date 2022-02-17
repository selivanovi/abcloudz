package com.example.spyfall.data.repository

import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.GameNotFoundException
import com.example.spyfall.utils.DatabaseNotResponding
import com.example.spyfall.utils.PLayerNotFoundException
import com.example.spyfall.utils.PLayersNotFoundException
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

    private fun getGameReference(gameId: String): DatabaseReference =
        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)


    private fun getPlayerReference(gameId: String, playerId: String): DatabaseReference =
        getGameReference(gameId).child(PLAYERS_KEY_REFERENCES).child(playerId)


    override suspend fun addGame(gameDomain: GameDomain) {
        getGameReference(gameDomain.gameId).setValue(gameDomain.toGame()).await()
    }

    override suspend fun getGame(gameId: String): GameDomain =
        suspendCancellableCoroutine { continuation ->
            val db = getGameReference(gameId)

            val listener = object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    continuation
                        .resumeWithException(
                            DatabaseNotResponding(Constants.DATABASE_NOT_RESPONDING)
                        )
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val game = snapshot.toGameDomain()
                        if (game != null) {
                            continuation.resume(game)
                        } else {
                            continuation.resumeWithException(
                                GameNotFoundException(
                                    Constants.GAME_NOT_FOUND_EXCEPTION
                                )
                            )
                        }
                    } catch (exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
            continuation.invokeOnCancellation { db.removeEventListener(listener) }
            db.addListenerForSingleValueEvent(listener)
        }

    override fun observeGame(gameId: String): Flow<GameDomain?> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.toGameDomain()
                    trySendBlocking(game)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(DatabaseNotResponding(Constants.DATABASE_NOT_RESPONDING))
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

        getPlayerReference(gameId, playerDomain.playerId).setValue(player).await()
    }

    override suspend fun deletePlayerInGame(gameId: String, playerId: String) {
        getPlayerReference(gameId, playerId).setValue(null).await()
    }

    override suspend fun deleteGame(gameId: String) {
        getGameReference(gameId).removeValue().await()
    }

    override fun observePlayersFromGame(gameId: String): Flow<List<PlayerDomain>> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val players = snapshot.children.map {
                        it.toPlayerDomain()
                    }
                    if (players.isEmpty()) {
                        trySendBlocking(players.filterNotNull())
                    } else {
                        close(PLayersNotFoundException(Constants.PLAYERS_NOT_FOUND_EXCEPTION))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    close(DatabaseNotResponding(Constants.DATABASE_NOT_RESPONDING))
                }
            }

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
    ): Flow<PlayerDomain> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val player: PlayerDomain? = snapshot.toPlayerDomain()
                    if (player != null) {
                        trySendBlocking(player)
                    } else {
                        close(PLayerNotFoundException(Constants.PLAYER_NOT_FOUND_EXCEPTION))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    close(DatabaseNotResponding(Constants.DATABASE_NOT_RESPONDING))
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
        val db = getGameReference(gameId).child(PLAYERS_KEY_REFERENCES)
        return db.get().await().children.mapNotNull { it.toPlayerDomain() }
    }

    override suspend fun setDurationForGames(gameId: String, time: Long) {
        firebaseDatabase.reference
            .child(GAMES_KEY_REFERENCES)
            .child(gameId)
            .child(DURATION_KEY_REFERENCES).setValue(time)
    }

    override suspend fun getDurationForGames(gameId: String): Result<Long> =
        runCatching {
            getGameReference(gameId).get().await().child(DURATION_KEY_REFERENCES)
                .getValue(Long::class.java)!!
        }

    override suspend fun setStatusForPlayerInGame(
        gameId: String,
        playerId: String,
        status: PlayerStatus?
    ) {
        getPlayerReference(
            gameId,
            playerId
        ).child(STATUS_KEY_REFERENCES).setValue(status).await()
    }

    override suspend fun setStatusForGame(gameId: String, status: GameStatus) {
        getGameReference(gameId).child(STATUS_KEY_REFERENCES).setValue(status).await()
    }

    override suspend fun setVoteForPlayerInGame(
        gameId: String,
        playerId: String,
        vote: PlayerDomain?
    ) {
        getPlayerReference(gameId, playerId).child(VOTE_KEY_REFERENCES)
            .setValue(vote?.playerId).await()
    }

    companion object {
        private const val GAMES_KEY_REFERENCES = "games"
        private const val PLAYERS_KEY_REFERENCES = "players"
        private const val DURATION_KEY_REFERENCES = "duration"
        private const val STATUS_KEY_REFERENCES = "status"
        private const val VOTE_KEY_REFERENCES = "vote"
    }
}
