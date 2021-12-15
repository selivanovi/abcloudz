package com.example.asyncoperations.model.network.characters


data class CharacterResponse(
    val created: String?,
    val episode: List<String>?,
    val gender: String?,
    val id: Long,
    val image: String?,
    val location: Location?,
    val name: String?,
    val origin: Origin?,
    val species: String?,
    val status: String?,
    val type: String?,
    val url: String?
)