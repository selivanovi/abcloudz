package com.example.asyncoperations.model

import com.example.asyncoperations.model.room.entities.Character
import com.example.asyncoperations.model.room.entities.Episode
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getCharacters(): Flow<List<Character>>
    fun getEpisodes(): Flow<List<Episode>>
    suspend fun deleteCharacters()
    suspend fun deleteEpisodes()
    suspend fun insertCharacters(characters: List<Character>)
    suspend fun updateEpisodes(episodes: List<Episode>)
}