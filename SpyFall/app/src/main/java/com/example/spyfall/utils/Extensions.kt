package com.example.spyfall.utils

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.UserDomain
import com.google.firebase.database.DataSnapshot
import java.util.UUID

fun generateRandomId(): String =
    UUID.randomUUID().toString().takeLast(5)

fun PlayerDomain.toPlayer(): Player =
    Player(
        name = this.name,
        status = this.status,
        role = this.role,
        vote = null
    )

fun GameDomain.toGame(): Game =
    Game(
        host = this.host,
        status = this.status,
        duration = this.duration
    )

fun UserDomain.toPlayerDomain(): PlayerDomain =
    PlayerDomain(
        playerId = this.userId,
        name = this.name,
        status = null,
        role = null,
        vote = null
    )

fun DataSnapshot.toPlayerDomain(): PlayerDomain? {
    val key = this.key
    val player = this.getValue(Player::class.java)
    return if (key != null && player != null) {
        PlayerDomain(
            playerId = key,
            name = player.name,
            status = player.status,
            role = player.role,
            vote = player.vote
        )
    } else null
}

fun DataSnapshot.toGameDomain(): GameDomain? {
    val key = this.key
    val game = this.getValue(Game::class.java)
    return if (key != null && game != null) {
        GameDomain(
            gameId = key,
            host = game.host,
            status = game.status,
            duration = game.duration)
    } else null
}

fun Long.toSeconds(): Long =
    this * 60
