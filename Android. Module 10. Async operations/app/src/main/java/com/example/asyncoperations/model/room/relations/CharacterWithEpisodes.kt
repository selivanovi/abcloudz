package com.example.asyncoperations.model.room.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.asyncoperations.model.room.entities.Character
import com.example.asyncoperations.model.room.entities.CharacterEpisodesCrossRef
import com.example.asyncoperations.model.room.entities.Episode

class CharacterWithEpisodes(
    @Embedded
    val character: Character,
    @Relation(
        parentColumn = "idCharacter",
        entityColumn = "idEpisode",
        associateBy = Junction(CharacterEpisodesCrossRef::class)
    )
    val episodes: List<Episode>
)