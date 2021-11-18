package com.example.localdatastorage.model.entities.ui

import com.example.localdatastorage.model.entities.json.BattersJson
import com.example.localdatastorage.model.entities.json.ToppingJson
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Topping

data class DonutUI(
    val id: Int,
    val name: String,
    val ppu: String,
    val type: String,
    val batter: String,
    val topping: String,
)