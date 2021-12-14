package com.example.asyncoperations.model

import com.example.asyncoperations.model.network.SimpleResponse
import com.example.asyncoperations.model.network.characters.CharacterResponse
import com.example.asyncoperations.model.network.episodes.EpisodeResponse

interface RemoteDataSource {

    suspend fun getCharactersByIds(listOfCharacters: String): SimpleResponse<List<CharacterResponse>>
    suspend fun getEpisodesPageByIds(listOfEpisode: String): SimpleResponse<List<EpisodeResponse>>
}