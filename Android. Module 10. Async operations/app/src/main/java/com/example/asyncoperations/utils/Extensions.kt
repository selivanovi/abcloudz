package com.example.networking.utils

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.networking.model.dao.*
import com.example.networking.model.network.characters.CharacterResponse
import com.example.networking.model.network.episodes.EpisodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun EpisodeResponse.toDTO() =
    Episode(
        id = this.id,
        name = this.name,
        episode = this.episode,
        airData = this.air_date
    )

fun CharacterResponse.toDTO(): Character =
    Character(
        id = this.id,
        name = this.name,
        origin = this.origin,
        episode = this.episode,
        image = this.image
    )

fun CharacterResponse.toAliveCharacter(): AliveCharacter =
    AliveCharacter(
        id = this.id,
        name = this.name,
        image = this.image,
        status = this.status,
        origin = this.origin,
        species = this.species
    )

fun CharacterResponse.toDeadCharacter(): DeadCharacter =
    DeadCharacter(
        id = this.id,
        name = this.name,
        image = this.image,
        status = this.status,
        origin = this.origin,
        species = this.species
    )

fun CharacterResponse.toUnknownCharacter(): UnknownCharacter =
    UnknownCharacter(
            id = this.id,
            name = this.name,
            image = this.image,
            status = this.status,
            origin = this.origin,
            species = this.species
        )
