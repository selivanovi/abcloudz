package com.example.localdatastorage.utils

import com.example.localdatastorage.model.json.entities.BatterJson
import com.example.localdatastorage.model.json.entities.DonutJson
import com.example.localdatastorage.model.json.entities.ToppingJson
import com.example.localdatastorage.model.room.entities.*

fun DonutJson.toDTO(): Donut =
    Donut(
        idDonut = this.id,
        name = this.name,
        ppu = this.ppu,
        type = this.type,
    )

fun BatterJson.toDTO(): Batter =
    Batter(
        idBatter = this.id,
        type = this.type
    )

fun ToppingJson.toDTO(): Topping =
    Topping(
        idTopping = this.id,
        type = this.type
    )

fun DonutJson.toBatterRelation(): List<DonutBatterCrossRef> {
    val mutableList = mutableListOf<DonutBatterCrossRef>()

    this.batters.batter.forEach { batterJson ->
        mutableList.add(DonutBatterCrossRef(this.id, batterJson.id))
    }

    return mutableList
}

fun DonutJson.toToppingRelation(): List<DonutToppingCrossRef> {
    val mutableList = mutableListOf<DonutToppingCrossRef>()

    this.topping.forEach { toppingJson ->
        mutableList.add(DonutToppingCrossRef(this.id, toppingJson.id))
    }

    return mutableList
}