package com.example.spyfall.utils

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.User
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun generateRandomId(): String =
    UUID.randomUUID().toString().takeLast(5)

fun PlayerDomain.toPlayer(): Player =
    Player(name = this.name, status = this.status, role = this.role)

fun GameDomain.toGame(): Game =
    Game(this.host, this.status, this.duration)

fun User.toPlayerDomain(): PlayerDomain =
    PlayerDomain(this.userId, this.name, null, null)

fun DataSnapshot.toPlayerDomain(): PlayerDomain? {
    val key = this.key
    val player = this.getValue(Player::class.java)
    return if (key != null && player != null){
        PlayerDomain(key, player.name, player.status, player.role, player.vote)
    } else null
}

fun DataSnapshot.toGameDomain(): GameDomain? {
    val key = this.key
    val game = this.getValue(Game::class.java)
    return if (key != null && game != null){
        GameDomain(key, game.host, game.status)
    } else null
}

suspend fun DatabaseReference.awaitsSingle(): DataSnapshot? =
    suspendCancellableCoroutine { continuation ->
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(GetDataException(Constants.GET_DATA_EXCEPTION))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    continuation.resume(snapshot)
                } catch (exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            }
        }
        continuation.invokeOnCancellation { this.removeEventListener(listener) }
        this.addListenerForSingleValueEvent(listener)
    }