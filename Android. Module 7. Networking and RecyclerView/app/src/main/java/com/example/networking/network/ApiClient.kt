package com.example.networking.network

import com.example.networking.model.CharactersPageResponse
import retrofit2.Call

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) {

    fun getCharactersPage(pageIndex: Int): Call<CharactersPageResponse> =
        rickAndMortyService.getCharactersPage(pageIndex)
}
