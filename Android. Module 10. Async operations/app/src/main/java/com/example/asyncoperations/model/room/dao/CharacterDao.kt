package com.example.asyncoperations.model.room.dao

import androidx.room.*
import com.example.asyncoperations.model.room.entities.Character
import com.example.asyncoperations.model.room.entities.CharacterEpisodesCrossRef
import com.example.asyncoperations.model.room.entities.Episode
import com.example.asyncoperations.model.room.relations.CharacterWithEpisodes

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterEpisodeCrossRefs(crossRefs: List<CharacterEpisodesCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<Episode>)

    @Transaction
    suspend fun insertCharacterWithEpisodes(
        episodes: List<Episode>,
        crossRefs: List<CharacterEpisodesCrossRef>
    ) {
        insertEpisodes(episodes)
        insertCharacterEpisodeCrossRefs(crossRefs)
    }

    @Query("SELECT * FROM character")
    suspend fun getCharacters(): List<Character>

    @Query("SELECT * FROM character WHERE idCharacter = (:idCharacter)")
    suspend fun getCharacterWithEpisodes(idCharacter: Int): CharacterEpisodesCrossRef

    @Query("DELETE FROM character")
    suspend fun deleteCharacters()

    @Query("DELETE FROM episode")
    suspend fun deleteEpisodes()

    @Query("DELETE FROM characterepisodescrossref")
    suspend fun deleteCharacterEpisodeCrossRef()

    @Transaction
    suspend fun deleteAll() {
        deleteCharacters()
        deleteEpisodes()
        deleteCharacterEpisodeCrossRef()
    }
}