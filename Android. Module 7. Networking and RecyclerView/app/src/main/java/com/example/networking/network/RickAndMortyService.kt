package com.example.networking.network

import com.example.networking.network.characters.CharactersPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyService {

    @GET("character/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int
    ): Response<CharactersPageResponse>
}
