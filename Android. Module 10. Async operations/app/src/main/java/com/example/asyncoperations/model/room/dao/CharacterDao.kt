package com.example.asyncoperations.model.room.dao

import androidx.room.*
import com.example.asyncoperations.model.room.entities.Character
import com.example.asyncoperations.model.room.entities.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<Episode>)


    @Query("SELECT * FROM character")
    fun getCharacters(): Flow<List<Character>>

    @Query("SELECT * FROM episode")
    fun getEpisodes(): Flow<List<Episode>>


    @Query("DELETE FROM character")
    suspend fun deleteCharacters()

    @Query("DELETE FROM episode")
    suspend fun deleteEpisodes()

    @Transaction
    suspend fun updateEpisodes(episodes: List<Episode>) {
        deleteEpisodes()
        insertEpisodes(episodes)
    }

}