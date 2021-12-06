package com.example.localdatastorage.model.entities.ui

import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Topping

data class DonutUI(
    val id: Int,
    val name: String,
    val ppu: String,
    val type: String,
    var batter: List<Batter>,
    var topping: List<Topping>,
    val allergy: String?,
) {
    fun getBattersString(delimiter: String? = null): String {
        val string = batter.map { it.type }
            .toString()
            .removePrefix("[")
            .removeSuffix("]")
        if (delimiter != null) {
            return string.replace(",", delimiter)
        }
        return string
    }

    fun getToppingsString(delimiter: String? = null): String {
        val string = topping.map { it.type }
            .toString()
            .removePrefix("[")
            .removeSuffix("]")
        if (delimiter != null) {
            return string.replace(",", delimiter)
        }
        return string
    }
}