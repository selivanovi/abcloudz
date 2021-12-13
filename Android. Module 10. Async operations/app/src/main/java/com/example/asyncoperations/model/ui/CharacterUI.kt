package com.example.asyncoperations.model.ui


data class CharacterUI(
    val idCharacter: Int,
    val image: String?,
    val name: String?,
    val species: String?,
    val status: String?,
    val episode: List<String>?
)