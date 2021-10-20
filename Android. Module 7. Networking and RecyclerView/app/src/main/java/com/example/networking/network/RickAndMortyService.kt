package com.example.networking.network

import com.example.networking.model.Character
import com.example.networking.model.CharactersList
import retrofit2.Call
import retrofit2.http.GET

interface RickAndMortyService {

    @GET("character/")
    fun getCharacters(): Call<CharactersList>
}
