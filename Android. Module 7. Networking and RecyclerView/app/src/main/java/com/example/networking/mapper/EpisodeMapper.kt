package com.example.networking.mapper

import com.example.networking.model.AliveCharacter
import com.example.networking.model.Episode
import com.example.networking.network.characters.CharacterResponse
import com.example.networking.network.episodes.EpisodeResponse

object EpisodeMapper {

    fun getFrom(
        response: EpisodeResponse
    ): Episode =
        Episode(
            id = response.id,
            name = response.name,
            episode = response.episode,
            airData = response.air_date
        )
}