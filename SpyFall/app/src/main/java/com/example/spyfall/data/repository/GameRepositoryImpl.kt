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
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GameRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
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

    override suspend fun addPlayerToGame(
        gameId: String,
        playerDomain: PlayerDomain
    ) {
        Log.d("GameRepository", playerDomain.toString())

        val player = playerDomain.toPlayer()

        playerReferences(gameId, playerDomain.playerId).setValue(player)
    }


    override suspend fun setRolesForPlayersInGame(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGame(gameId: String) {
        gameReferences(gameId).setValue(null)
    }

    override fun getObservePlayersFromGame(gameId: String): Flow<Result<List<PlayerDomain>>> =
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

//    override suspend fun getPlayersFromGame(gameId: String): List<PlayerDomain> =
//        suspendCancellableCoroutine { coroutine ->
//            firebaseDatabase.reference
//                .child(GAMES_KEY_REFERENCES)
//                .child(gameId).child(PLAYERS_KEY_REFERENCES).get()
//                .addOnSuccessListener { dataShapshot ->
//                    val players = dataShapshot.children.mapNotNull { it.toPlayerDomain() }
//                    coroutine.resume(players)
//                }
//                .addOnFailureListener {
//                    coroutine.resumeWithException(GetDataException(Constants.GET_DATA_EXCEPTION))
//                }
//        }

    override suspend fun getPlayersFromGame(gameId: String): List<PlayerDomain> {
        val db = gameReferences(gameId).child(PLAYERS_KEY_REFERENCES)
        return db.awaitsSingle()!!.children.mapNotNull { it.toPlayerDomain() }
    }

    override suspend fun setTimeForGames(gameId: String, time: Int) {
        firebaseDatabase.reference
            .child(GAMES_KEY_REFERENCES)
            .child(gameId)
            .child(DURATION_KEY_REFERENCES).setValue(time)
    }

    override suspend fun getDurationForGames(gameId: String): Int {
        return gameReferences(gameId).awaitsSingle()!!.child(DURATION_KEY_REFERENCES)
            .getValue(Int::class.java)!!
    }

    override suspend fun setStatusForPlayerInGame(
        gameId: String,
        playerId: String,
        status: PlayerStatus
    ) {
        playerReferences(gameId, playerId).child(STATUS_KEY_REFERENCES).setValue(status)
    }

    override suspend fun setStatusForGame(gameId: String, status: GameStatus) {
        gameReferences(gameId).child(STATUS_KEY_REFERENCES).setValue(status)
    }

    companion object {
        private const val KEY_CURRENT_GAME = "key_current_game"
        private const val GAMES_KEY_REFERENCES = "games"
        private const val PLAYERS_KEY_REFERENCES = "players"
        private const val DURATION_KEY_REFERENCES = "duration"
        private const val STATUS_KEY_REFERENCES = "status"
    }
}