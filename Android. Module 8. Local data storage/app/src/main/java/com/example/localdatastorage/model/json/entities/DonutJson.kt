package com.example.localdatastorage.model.json.entities


data class DonutJson(
    val id: Int?,
    val name: String?,
    val ppu: Double?,
    val type: String?,
    val batters: BattersJson?,
    val topping: List<ToppingJson>?,
)