package com.example.localdatastorage.utils

import com.example.localdatastorage.model.entities.json.BatterJson
import com.example.localdatastorage.model.entities.json.DonutJson
import com.example.localdatastorage.model.entities.json.ToppingJson
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef

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

fun Donut.toDonutUI() =
    DonutUI(
        id = this.idDonut,
        name = this.name,
        ppu = this.ppu.toString(),
        type = this.type
    )
