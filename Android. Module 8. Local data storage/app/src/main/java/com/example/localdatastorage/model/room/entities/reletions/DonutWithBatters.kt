package com.example.localdatastorage.model.room.entities.reletions

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef

data class DonutWithBatters(
    @Embedded val donut: Donut,
    @Relation(
        parentColumn = "idDonut",
        entityColumn = "idBatter",
        associateBy = Junction(DonutBatterCrossRef::class)
    )
    val batter: List<Batter>
)
