package com.example.networking.model.network.episodes

data class EpisodeResponse(
    val air_date: String = "",
    val characters: List<Any> = listOf(),
    val created: String = "",
    val episode: String = "",
    val id: Int = 0,
    val name: String = "",
    val url: String = ""
)