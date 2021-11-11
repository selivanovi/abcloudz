package com.example.localdatastorage.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Donut(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String?,
    val ppu: Double?,
    val type: String?,
    val batters: Batters?,
    val topping: List<Topping>?,
)