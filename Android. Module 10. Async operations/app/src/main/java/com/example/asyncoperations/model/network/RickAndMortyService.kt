package com.example.asyncoperations.model.network

import com.example.asyncoperations.model.network.characters.CharacterResponse
import com.example.asyncoperations.model.network.episodes.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyService {

    @GET("character/{listOfCharacters}")
    suspend fun getCharactersByIds(
        @Path("listOfCharacters") listOfCharacters: String
    ): Response<List<CharacterResponse>>

    @GET("episode/{listOfEpisodes}")
    suspend fun getEpisodesByIds(
        @Path("listOfEpisodes") listOfEpisodes: String
    ): Response<List<EpisodeResponse>>

}
