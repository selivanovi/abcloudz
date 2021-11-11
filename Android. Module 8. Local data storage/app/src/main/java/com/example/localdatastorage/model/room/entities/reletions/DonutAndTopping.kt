package com.example.localdatastorage.model.room.entities.reletions

import androidx.room.Embedded
import androidx.room.Relation
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Topping

data class DonutAndTopping (
    @Embedded val donut: Donut,
    @Relation(
        parentColumn = "id",
        entityColumn = "donut"
    )
    val topping: List<Topping>
)