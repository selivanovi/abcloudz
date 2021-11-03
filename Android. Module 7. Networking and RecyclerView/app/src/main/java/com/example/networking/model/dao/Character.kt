package com.example.networking.model.dao

import com.example.networking.model.network.characters.Origin

data class Character(
    val id: Int = 0,
    val name: String = "",
    val origin: Origin = Origin(),
    val image: String = "",
    val species: String = "",
    val status: String = "",
    val episode: List<String> = listOf()
)