package com.example.networking.model.dao

import com.example.networking.model.network.characters.Origin

data class Character(
    val id: Int? = null,
    val name: String? = null,
    val origin: Origin? = null,
    val image: String? = null,
    val species: String? = null,
    val status: String? = null,
    val episode: List<String>? = null
)