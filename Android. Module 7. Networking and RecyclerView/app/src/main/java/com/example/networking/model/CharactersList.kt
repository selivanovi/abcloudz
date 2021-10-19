package com.example.networking.model

import com.google.gson.annotations.SerializedName

data class CharactersList(
    @SerializedName("results")
    val charactersList: List<Character>
)
