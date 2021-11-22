package com.example.localdatastorage.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Donut(
    @PrimaryKey(autoGenerate = true)
    val idDonut: Int,
    val name: String,
    val ppu: Double,
    val type: String,
)