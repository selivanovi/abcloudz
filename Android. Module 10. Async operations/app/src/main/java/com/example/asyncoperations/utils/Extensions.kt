package com.example.asyncoperations.utils

import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.model.ui.EpisodeUI
import com.example.networking.model.network.characters.CharacterResponse
import com.example.networking.model.network.episodes.EpisodeResponse

fun EpisodeResponse.toUI() =
    EpisodeUI(
        idEpisode = this.id,
        name = this.name,
        episode = this.episode,
        airData = this.air_date
    )

fun CharacterResponse.toUI() =
    CharacterUI(
        idCharacter = this.id,
        image = this.image,
        name = this.name,
        species = this.species,
        status = this.status,
        episode = this.episode
    )
