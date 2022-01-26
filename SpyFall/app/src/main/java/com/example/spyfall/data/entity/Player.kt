package com.example.spyfall.data.entity

import com.example.spyfall.utils.generateRandomID

data class Player(
    val playerID: String = generateRandomID(),
    val name: String,
    val status: PlayerStatus?
)
