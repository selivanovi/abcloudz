package com.example.spyfall.domain.entity

import com.example.spyfall.data.entity.GameStatus

data class GameDomain(
    val gameId: String,
    val host: String? = null,
    val status: GameStatus? = null,
    val duration: Int? = null,
) {

}