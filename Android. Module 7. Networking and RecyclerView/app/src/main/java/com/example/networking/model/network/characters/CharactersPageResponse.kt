package com.example.networking.model.network.characters

import Info

data class CharactersPageResponse(
    val info: Info? = null,
    val results: List<CharacterResponse>? = null
)
