package com.example.localdatastorage.model.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["idDonut", "idBatter"])
data class DonutBatterCrossRef(
    val idDonut: Int,
    val idBatter: Int
)
