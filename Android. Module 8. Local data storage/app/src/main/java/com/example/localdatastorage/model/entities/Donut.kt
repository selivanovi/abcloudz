package com.example.localdatastorage.model.entities

data class Donut(
    val batters: Batters?,
    val id: String?,
    val name: String?,
    val ppu: Double?,
    val topping: List<Topping>?,
    val type: String?
)