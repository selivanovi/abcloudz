package com.example.networking.model.mapper

import com.example.networking.model.dao.Episode
import com.example.networking.model.network.episodes.EpisodeResponse

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