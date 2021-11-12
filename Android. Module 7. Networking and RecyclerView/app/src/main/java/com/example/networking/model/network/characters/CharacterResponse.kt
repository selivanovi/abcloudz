package com.example.networking.model.network.characters

import com.example.networking.model.dao.Character

data class CharacterResponse(
    val created: String? = null,
    val episode: List<String>? = null,
    val gender: String? = null,
    val id: Int = 0,
    val image: String? = null,
    val location: Location? = null,
    val name: String? = null,
    val origin: Origin? = null,
    val species: String? = null,
    val status: String? = null,
    val type: String? = null,
    val url: String? = null
)