package com.example.localdatastorage.model.room.entities

import androidx.room.Entity

@Entity
data class Topping(
    val id: String?,
    val type: String?,
    val donut: Int?
)