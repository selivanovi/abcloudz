package com.example.localdatastorage.utils

import com.example.localdatastorage.model.json.entities.BatterJson
import com.example.localdatastorage.model.json.entities.DonutJson
import com.example.localdatastorage.model.json.entities.ToppingJson
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Topping

fun DonutJson.toDTO(): Donut =
    Donut(
        id = this.id,
        name = this.name,
        ppu = this.ppu,
        type = this.type,
        batter = this.batters?.batter?.map { it.toDTO() },
        topping = this.topping?.map { it.toDTO() }
    )

fun BatterJson.toDTO(): Batter =
    Batter(
        id = this.id,
        type = this.type
    )

fun ToppingJson.toDTO(): Topping =
    Topping(
        id = this.id,
        type = this.type
    )