package com.example.spyfall.utils

import com.example.spyfall.data.entity.Game
import com.example.spyfall.data.entity.Player
import com.example.spyfall.domain.entity.GameDomain
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.User
import com.google.firebase.database.DataSnapshot
import java.util.*

fun generateRandomId(): String =
    UUID.randomUUID().toString().takeLast(5)

fun PlayerDomain.toPlayer(): Player =
    Player(name = this.name, status = this.status, role = this.role)

fun User.toPlayerDomain(): PlayerDomain =
    PlayerDomain(this.userId, this.name, null, null)

fun DataSnapshot.toPlayerDomain(): PlayerDomain? {
    val key = this.key
    val player = this.getValue(Player::class.java)
    return if (key != null && player != null){
        PlayerDomain(key, player.name, player.status, player.role)
    } else null
}

fun DataSnapshot.toGameDomain(): GameDomain? {
    val key = this.key
    val game = this.getValue(Game::class.java)
    return if (key != null && game != null){
        GameDomain(key, game.host, game.status)
    } else null
}