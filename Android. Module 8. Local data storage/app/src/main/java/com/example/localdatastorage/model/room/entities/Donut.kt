package com.example.localdatastorage.model.room.entities


data class Donut(
    val id: Int?,
    val name: String?,
    val ppu: Double?,
    val type: String?,
    val batter: List<Batter>?,
    val topping: List<Topping>?,
)