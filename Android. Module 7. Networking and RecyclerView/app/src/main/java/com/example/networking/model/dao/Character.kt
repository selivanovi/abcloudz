package com.example.networking.model.dao

import com.example.networking.model.network.characters.Origin

class Character(
    val id: Int,
    val name: String?,
    val origin: Origin?,
    val image: String?,
    val episode: List<String>?
)