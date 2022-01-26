package com.example.spyfall.utils

import com.example.spyfall.data.entity.Player
import com.example.spyfall.data.entity.User
import java.util.*

fun generateRandomID(): String =
    UUID.randomUUID().toString().takeLast(5)

fun User.toPlayer(): Player =
    Player(this.userId, this.name, null)