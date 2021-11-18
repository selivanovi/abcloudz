package com.example.localdatastorage.model.room

import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBatters
import com.example.localdatastorage.model.room.entities.reletions.DonutWithToppings
import kotlinx.coroutines.flow.Flow


class DonutsRepository(
    private val dataBase: DonutDataBase
) {

    suspend fun insertDonut(vararg donut: Donut) =
        dataBase.donutsDao.insertDonut(*donut)

    suspend fun insertBatter(vararg batter: Batter) =
        dataBase.donutsDao.insertBatter(*batter)

    suspend fun insertTopping(vararg topping: Topping) =
        dataBase.donutsDao.insertTopping(*topping)

    suspend fun insertDonutBatterCrossRef(vararg donutBatterCrossRef: DonutBatterCrossRef) =
        dataBase.donutsDao.insertDonutBatterCrossRef(*donutBatterCrossRef)

    suspend fun insertDonutToppingCrossRef(vararg donutToppingCrossRef: DonutToppingCrossRef) =
        dataBase.donutsDao.insertDonutToppingCrossRef(*donutToppingCrossRef)

    fun getBattersOfDonut(idDonut: Int): Flow<DonutWithBatters> =
        dataBase.donutsDao.getBattersOfDonut(idDonut)

    fun getToppingsOfDonut(idDonut: Int):Flow<DonutWithToppings> =
        dataBase.donutsDao.getToppingsOfDonut(idDonut)
}