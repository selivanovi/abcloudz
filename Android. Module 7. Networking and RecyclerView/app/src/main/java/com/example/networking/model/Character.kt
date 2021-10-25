package com.example.networking.model

import com.example.networking.network.characters.Location
import com.example.networking.network.characters.Origin

data class Character(
    val episode: List<String> = listOf(),
    val gender: String = "",
    val id: Int = 0,
    val image: String = "",
    val location: Location = Location(),
    val name: String = "",
    val origin: Origin = Origin(),
    val species: String = "",
    val status: String = "",
    val type: String = "",
    val url: String = ""
) {
}