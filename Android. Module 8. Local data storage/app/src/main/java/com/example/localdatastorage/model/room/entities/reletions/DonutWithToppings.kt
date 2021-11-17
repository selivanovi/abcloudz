package com.example.localdatastorage.model.room.entities.reletions

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.model.room.entities.Topping

data class DonutWithToppings(
    @Embedded val donut: Donut,
    @Relation(
        parentColumn = "idDonut",
        entityColumn = "idTopping",
        associateBy = Junction(DonutToppingCrossRef::class)
    )
    val topping: List<Topping>
)
