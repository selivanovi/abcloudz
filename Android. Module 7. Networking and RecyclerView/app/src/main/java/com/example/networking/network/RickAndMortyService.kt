package com.example.networking.network

import com.example.networking.network.characters.CharactersPageResponse
import com.example.networking.network.episodes.EpisodeResponse
import com.example.networking.network.episodes.ListOfEpisodesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {

    @GET("character/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int
    ): Response<CharactersPageResponse>

    @GET("episode/{listOfEpisodes}")
    suspend fun getEpisodesPageByIds(
        @Path("listOfEpisodes") listOfEpisode: String
    ): Response<List<EpisodeResponse>>
}
