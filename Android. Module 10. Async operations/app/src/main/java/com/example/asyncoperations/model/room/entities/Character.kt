package com.example.asyncoperations.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey
    val idCharacter: Long,
    val image: String?,
    val name: String?,
    val species: String?,
    val status: String?,
)