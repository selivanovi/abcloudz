package com.example.spyfall.data.entity

data class Game(
    val host: String? = null,
    val status: GameStatus? = null,
    val duration: Int? = null,
    val vote: String? = null
)