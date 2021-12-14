package com.example.asyncoperations.utils

import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.model.ui.EpisodeUI
import com.example.asyncoperations.model.network.characters.CharacterResponse
import com.example.asyncoperations.model.network.episodes.EpisodeResponse
import com.example.asyncoperations.model.room.entities.Character
import com.example.asyncoperations.model.room.entities.Episode

fun EpisodeResponse.toUI() =
    EpisodeUI(
        idEpisode = this.id,
        name = this.name,
        episode = this.episode,
        airDate = this.air_date
    )

fun CharacterResponse.toUI() =
    CharacterUI(
        idCharacter = this.id,
        image = this.image,
        name = this.name,
        species = this.species,
        status = this.status,
    )

fun EpisodeResponse.toDTO() =
    Episode(
        idEpisode = this.id,
        name = this.name,
        episode = this.episode,
        airDate = this.air_date
    )

fun CharacterResponse.toDTO() =
    Character(
        idCharacter = this.id,
        image = this.image,
        name = this.name,
        species = this.species,
        status = this.status,
    )

fun Episode.toUI() =
    EpisodeUI(
        idEpisode = this.idEpisode,
        name = this.name,
        episode = this.episode,
        airDate = this.airDate
    )

fun Character.toUI() =
    CharacterUI(
        idCharacter = this.idCharacter,
        image = this.image,
        name = this.name,
        species = this.species,
        status = this.status,
    )