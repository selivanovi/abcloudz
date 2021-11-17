package com.example.localdatastorage.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Topping(
    @PrimaryKey(autoGenerate = true)
    val idTopping: Int,
    val type: String?,
)