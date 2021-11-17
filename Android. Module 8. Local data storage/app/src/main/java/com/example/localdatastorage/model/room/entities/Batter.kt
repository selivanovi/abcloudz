package com.example.localdatastorage.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Batter(
    @PrimaryKey(autoGenerate = true)
    val idBatter: Int,
    val type: String?
)