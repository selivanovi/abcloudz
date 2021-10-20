package com.example.networking

import com.example.networking.model.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyService {

    @GET("character/{id}")
    fun getCharacterById(@Path("id") characterId: Int): Call<Character>
}