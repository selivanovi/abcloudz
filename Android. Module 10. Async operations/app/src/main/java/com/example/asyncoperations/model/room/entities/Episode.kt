package com.example.asyncoperations.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Episode(
    @PrimaryKey
    val idEpisode: Int,
    val airData: String?,
    val episode: String?,
    val name: String?,
)