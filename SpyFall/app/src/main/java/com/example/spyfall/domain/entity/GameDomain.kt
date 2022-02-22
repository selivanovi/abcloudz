package com.example.spyfall.domain.entity

import com.example.spyfall.data.entity.GameStatus

data class GameDomain(
    val gameId: String,
    val host: String,
    val status: GameStatus,
    val duration: Long,
)
