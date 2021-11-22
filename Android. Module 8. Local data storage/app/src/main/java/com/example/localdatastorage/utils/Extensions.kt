package com.example.localdatastorage.utils

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.localdatastorage.model.entities.json.BatterJson
import com.example.localdatastorage.model.entities.json.DonutJson
import com.example.localdatastorage.model.entities.json.ToppingJson
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBattersAndToppings
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.retry

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

fun DonutUI.getDonut(): Donut =
    Donut(
        this.id,
        this.name,
        this.ppu.toDouble(),
        this.type
    )

fun DonutWithBattersAndToppings.toDonutUI() =
    DonutUI(
        id = this.donut.idDonut,
        name = this.donut.name,
        ppu = this.donut.ppu.toString(),
        type = this.donut.type,
        batter = this.batter,
        topping = this.topping
    )


fun <T> Flow<T>.retryIn(scope: CoroutineScope): Job =
    this.retry { error ->
        val handler = scope.coroutineContext[CoroutineExceptionHandler.Key]
        handler?.handleException(scope.coroutineContext, error)
        true
    }.launchIn(scope)
