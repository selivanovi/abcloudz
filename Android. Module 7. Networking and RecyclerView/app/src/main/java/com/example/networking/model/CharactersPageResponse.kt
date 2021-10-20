package com.example.networking.model

import com.google.gson.annotations.SerializedName

data class CharactersPageResponseList(
    val info: Info =  Info(),
    val results: List<CharacterResponse>
) {

    data class Info(
        val count: Int = 0,
        val pages: Int = 0,
        val next: String? = null,
        val prev: String? = null
    )
}
