package com.example.asyncoperations.model.room.entities

import androidx.room.Entity

@Entity(primaryKeys = ["idCharacter", "idEpisode"])
data class CharacterEpisodesCrossRef(
    val idCharacter: Int,
    val idEpisode: Int
)
