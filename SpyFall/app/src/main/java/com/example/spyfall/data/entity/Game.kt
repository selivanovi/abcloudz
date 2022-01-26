package com.example.spyfall.data.entity

import com.example.spyfall.utils.generateRandomID

data class Game(
    val gameID: String = generateRandomID() ,
    val host: String?,
    val status: GameStatus?,
    val players: List<Player>?
)
