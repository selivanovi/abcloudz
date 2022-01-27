package com.example.spyfall.utils

import com.example.spyfall.data.entity.Player
import com.example.spyfall.domain.entity.PlayerDomain
import com.example.spyfall.domain.entity.User
import java.util.*

fun generateRandomId(): String =
    UUID.randomUUID().toString().takeLast(5)

fun PlayerDomain.toPlayer(): Player =
    Player(name = this.name, status = this.status, role = this.role)

fun User.toPlayerDomain(): PlayerDomain =
    PlayerDomain(this.userId, this.name, null, null)