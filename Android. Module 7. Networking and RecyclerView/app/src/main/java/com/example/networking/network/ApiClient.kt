package com.example.networking.network

import com.example.networking.model.Character
import com.example.networking.model.CharactersList
import retrofit2.Call

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) {


    fun getCharacters(): Call<CharactersList> =
        rickAndMortyService.getCharacters()
}
