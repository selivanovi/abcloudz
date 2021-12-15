package com.example.asyncoperations.model.network.episodes

import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("air_date")
    val airDate: String?,
    val characters: List<Any>?,
    val created: String?,
    val episode: String?,
    val id: Long,
    val name: String?,
    val url: String?
)

