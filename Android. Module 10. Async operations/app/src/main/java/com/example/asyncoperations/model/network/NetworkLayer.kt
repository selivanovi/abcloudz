package com.example.networking.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkLayer {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val rickAndMortyService: RickAndMortyService =
        retrofit.create(RickAndMortyService::class.java)


    val apiService = ApiClient(rickAndMortyService)
}