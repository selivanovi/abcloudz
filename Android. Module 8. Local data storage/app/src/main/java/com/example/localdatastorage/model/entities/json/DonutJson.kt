package com.example.localdatastorage.model.entities.json


data class DonutJson(
    val id: Int,
    val name: String,
    val ppu: Double,
    val type: String,
    val batters: BattersJson,
    val topping: List<ToppingJson>,
    val allergy: String?
)