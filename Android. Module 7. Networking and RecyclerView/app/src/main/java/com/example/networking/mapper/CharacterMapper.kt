package com.example.networking.mapper

import com.example.networking.model.Character
import com.example.networking.network.characters.CharacterResponse

object CharacterMapper {

    fun getFrom(
        response: CharacterResponse
    ): Character =
        Character(
            id = response.id,
            name = response.name,
            image = response.image,
            status = response.status,
            location = response.location,
            origin = response.origin,
            species = response.species
        )
}