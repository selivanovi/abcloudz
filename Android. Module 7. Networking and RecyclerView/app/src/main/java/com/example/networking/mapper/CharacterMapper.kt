package com.example.networking.mapper

import com.example.networking.model.AliveCharacter
import com.example.networking.model.DeadCharacter
import com.example.networking.model.UnknownCharacter
import com.example.networking.network.characters.CharacterResponse

object AliveCharacterMapper {

    fun getFrom(
        response: CharacterResponse
    ): AliveCharacter =
        AliveCharacter(
            id = response.id,
            name = response.name,
            image = response.image,
            status = response.status,
            origin = response.origin,
            species = response.species
        )
}

object DeadCharacterMapper {

    fun getFrom(
        response: CharacterResponse
    ): DeadCharacter =
        DeadCharacter(
            id = response.id,
            name = response.name,
            status = response.status,
            origin = response.origin,
            species = response.species
        )
}

object UnknownCharacterMapper {

    fun getFrom(
        response: CharacterResponse
    ): UnknownCharacter =
        UnknownCharacter(
            id = response.id,
            name = response.name,
            image = response.image,
            status = response.status,
            origin = response.origin,
            species = response.species
        )
}
