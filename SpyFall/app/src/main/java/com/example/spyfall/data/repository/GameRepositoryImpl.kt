package com.example.spyfall.data.repository

import android.util.Log
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.utils.*
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GameRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : GameRepository {

    override var currentPlayer: PlayerDomain? = null
    override var currentGame: GameDomain? = null

    private val gameReferences: (gameId: String) -> DatabaseReference = { gameId ->
        firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(gameId)
    }
    private val playerReferences: (gameId: String, playerId: String) -> DatabaseReference =
        { gameId, playerId ->
            gameReferences(gameId).child(PLAYERS_KEY_REFERENCES).child(playerId)
        }

    override suspend fun addGame(gameDomain: GameDomain) {

        currentGame = gameDomain

        Log.d("GameRepository", "$currentGame")

        gameReferences(gameDomain.gameId).setValue(gameDomain.toGame())
    }

    override suspend fun getGame(gameId: String): GameDomain =
        suspendCancellableCoroutine { continuation ->
            val db = gameReferences(gameId)

            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                    continuation.resumeWithException(GetDataException(Constants.GET_DATA_EXCEPTION))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val game = snapshot.toGameDomain()
                        game?.let {
                            continuation.resume(it)
                        }
                    } catch (exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
            continuation.invokeOnCancellation { db.removeEventListener(listener) }
            db.addListenerForSingleValueEvent(listener)
        }

    override fun observeGame(): Flow<Result<GameDomain>> =
        callbackFlow {

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.toGameDomain()!!
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

            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(currentGame!!.gameId)
                .addValueEventListener(
                    valueEventListener
                )

            awaitClose {
                firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(currentGame!!.gameId)
                    .removeEventListener(valueEventListener)
            }
        }


    override suspend fun addPlayerToGame(
        playerDomain: PlayerDomain
    ) {


        currentPlayer = playerDomain

        Log.d("GameRepository", currentPlayer.toString())


        val player = playerDomain.toPlayer()

        playerReferences(currentPlayer!!.playerId, playerDomain.playerId).setValue(player)
    }

    override suspend fun updatePlayerInGame(playerDomain: PlayerDomain) {
        if (currentPlayer?.playerId == playerDomain.playerId) {
            currentPlayer = playerDomain
        }

        val player = playerDomain.toPlayer()

        playerReferences(currentGame!!.gameId, playerDomain.playerId).setValue(player)
    }


    override suspend fun deleteGame() {
        gameReferences(currentGame!!.gameId).setValue(null)
        currentGame = null
    }

    override fun observePlayersFromGame(): Flow<Result<List<PlayerDomain>>> =
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

            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(currentGame!!.gameId)
                .child(
                    PLAYERS_KEY_REFERENCES
                ).addValueEventListener(
                valueEventListener
            )

            awaitClose {
                firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(currentGame!!.gameId)
                    .child(
                        PLAYERS_KEY_REFERENCES
                    ).removeEventListener(valueEventListener)
            }
        }

    override fun observeCurrentPlayer(): Flow<Result<PlayerDomain>> =
        callbackFlow {

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val player = snapshot.toPlayerDomain()!!
                    this@callbackFlow.trySendBlocking(Result.success(player))
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(
                        Result.failure(
                            GetDataException(Constants.GET_DATA_EXCEPTION)
                        )
                    )
                }
            }

            firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(currentGame!!.gameId)
                .child(
                    PLAYERS_KEY_REFERENCES
                ).child(currentPlayer!!.playerId).addValueEventListener(
                valueEventListener
            )

            awaitClose {
                firebaseDatabase.reference.child(GAMES_KEY_REFERENCES).child(currentGame!!.gameId)
                    .child(
                        PLAYERS_KEY_REFERENCES
                    ).child(currentPlayer!!.playerId).removeEventListener(valueEventListener)
            }
        }


    override suspend fun getPlayersFromGame(): List<PlayerDomain> {
        val db = gameReferences(currentGame!!.gameId).child(PLAYERS_KEY_REFERENCES)
        return db.get().await().children.mapNotNull { it.toPlayerDomain() }
    }

    override suspend fun setTimeForGames(time: Int) {
        firebaseDatabase.reference
            .child(GAMES_KEY_REFERENCES)
            .child(currentGame!!.gameId)
            .child(DURATION_KEY_REFERENCES).setValue(time)
    }

    override suspend fun getDurationForGames(): Int {
        return gameReferences(currentGame!!.gameId).get().await().child(DURATION_KEY_REFERENCES)
            .getValue(Int::class.java)!!
    }

    override suspend fun setStatusForCurrentPlayerInGame(
        status: PlayerStatus
    ) {
        playerReferences(
            currentGame!!.gameId,
            currentPlayer!!.playerId
        ).child(STATUS_KEY_REFERENCES).setValue(status)
    }

    override suspend fun setStatusForGame(status: GameStatus) {
        gameReferences(currentGame!!.gameId).child(STATUS_KEY_REFERENCES).setValue(status)
    }

    companion object {
        private const val GAMES_KEY_REFERENCES = "games"
        private const val PLAYERS_KEY_REFERENCES = "players"
        private const val DURATION_KEY_REFERENCES = "duration"
        private const val STATUS_KEY_REFERENCES = "status"
    }
}