package com.example.localdatastorage.model.entities.ui

import com.example.localdatastorage.model.room.entities.Topping

data class DonutUI(
    val id: Int,
    val name: String? = null,
    val ppu: String? = null,
    val type: String? = null,
    var batter: String? = null,
    var topping: String? = null,
)