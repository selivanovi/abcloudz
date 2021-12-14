package com.example.asyncoperations.model.room

import com.example.asyncoperations.model.LocalDataSource
import com.example.asyncoperations.model.room.dao.CharacterDao
import com.example.asyncoperations.model.room.entities.Character
import com.example.asyncoperations.model.room.entities.Episode
import kotlinx.coroutines.flow.Flow

class RoomSource(
    private val dao: CharacterDao
) : LocalDataSource {

    override fun getCharacters(): Flow<List<Character>> {
        return dao.getCharacters()
    }

    override  fun getEpisodes(): Flow<List<Episode>> {
        return dao.getEpisodes()
    }

    override suspend fun deleteCharacters() {
        dao.deleteCharacters()
    }

    override suspend fun deleteEpisodes() {
        dao.deleteEpisodes()
    }

    override suspend fun insertCharacters(characters: List<Character>) {
        dao.insertCharacters(characters)
    }

    override suspend fun updateEpisodes(episodes: List<Episode>) {
        dao.updateEpisodes(episodes)
    }


}