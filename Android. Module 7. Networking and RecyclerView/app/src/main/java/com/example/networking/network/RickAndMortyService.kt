package com.example.networking.network

import com.example.networking.model.CharactersPageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyService {

    @GET("character/")
    abstract fun getCharactersPage(
        @Query("page") pageIndex: Int
    ): Call<CharactersPageResponse>
}
