package com.example.networking.model.network

import com.example.networking.model.network.characters.CharacterResponse
import com.example.networking.model.network.characters.CharactersPageResponse
import com.example.networking.model.network.episodes.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {

    @GET("character/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int
    ): Response<CharactersPageResponse>

    @GET("character/{character-id}")
    suspend fun getCharacterById(
        @Path("character-id") idOfCharacter: Int
    ): Response<CharacterResponse>

    @GET("episode/{listOfEpisodes}")
    suspend fun getEpisodesPageByIds(
        @Path("listOfEpisodes") listOfEpisode: String
    ): Response<List<EpisodeResponse>>

}
