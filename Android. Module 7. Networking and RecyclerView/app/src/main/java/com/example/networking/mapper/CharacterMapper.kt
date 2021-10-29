package com.example.networking.mapper

import com.example.networking.model.AliveCharacter
import com.example.networking.model.Character
import com.example.networking.model.DeadCharacter
import com.example.networking.model.UnknownCharacter
import com.example.networking.network.characters.CharacterResponse

object CharacterMapper {

    fun getFrom(
        response: CharacterResponse
    ): Character =
        Character(
            id = response.id,
            name = response.name,
            origin = response.origin,
            episode = response.episode,
            image = response.image
        )
}

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
            image = response.image,
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
