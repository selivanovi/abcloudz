package com.example.networking

import com.example.networking.model.CharactersPageResponse
import retrofit2.Call
import retrofit2.http.GET

interface RickAndMortyService {

    @GET("character")
    fun getCharacterById(): Call<CharactersPageResponse>
}