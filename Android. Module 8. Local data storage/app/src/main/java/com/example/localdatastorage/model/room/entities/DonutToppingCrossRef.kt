package com.example.localdatastorage.model.room.entities

import androidx.room.Entity

@Entity(primaryKeys = ["idDonut", "idTopping"])
data class DonutToppingCrossRef(
    val idDonut: Int,
    val idTopping: Int
)
