package com.example.localdatastorage.model.room

import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBatters
import com.example.localdatastorage.model.room.entities.reletions.DonutWithToppings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion


class DonutsRepository(
    private val dataBase: DonutDataBase
) {

    suspend fun insertDonutsWithToppingsAndButters(
        donuts: List<Donut>,
        batters: List<Batter>,
        toppings: List<Topping>,
        donutBatterCrossRefs: List<DonutBatterCrossRef>,
        donutToppingCrossRefs: List<DonutToppingCrossRef>
    ) = dataBase.donutsDao.insertDonutsWithBattersAndToppings(donuts, batters, toppings, donutBatterCrossRefs, donutToppingCrossRefs)

    suspend fun insertDonut(donut: Donut) =
        dataBase.donutsDao.insertDonut(donut)

    suspend fun insertBatter(batter: Batter) =
        dataBase.donutsDao.insertBatter(batter)

    suspend fun insertTopping(topping: Topping) =
        dataBase.donutsDao.insertTopping(topping)

    suspend fun insertDonutBatterCrossRef(donutBatterCrossRef: DonutBatterCrossRef) =
        dataBase.donutsDao.insertDonutBatterCrossRef(donutBatterCrossRef)

    suspend fun insertDonutToppingCrossRef(donutToppingCrossRef: DonutToppingCrossRef) =
        dataBase.donutsDao.insertDonutToppingCrossRef(donutToppingCrossRef)

    suspend fun insertDonutBatterCrossRefs(donutBatterCrossRefs: List<DonutBatterCrossRef>) =
        dataBase.donutsDao.insertDonutBatterCrossRefs(donutBatterCrossRefs)

    suspend fun insertDonutToppingCrossRefs(donutToppingCrossRefs: List<DonutToppingCrossRef>) =
        dataBase.donutsDao.insertDonutToppingCrossRefs(donutToppingCrossRefs)

    fun getBattersOfDonut(idDonut: Int): Flow<DonutWithBatters> =
        dataBase.donutsDao.getBattersOfDonut(idDonut)

    fun getToppingsOfDonut(idDonut: Int): Flow<DonutWithToppings> =
        dataBase.donutsDao.getToppingsOfDonut(idDonut)

    fun getDonutsAndBatters(): Flow<List<DonutWithBatters>> =
        dataBase.donutsDao.getBattersOfDonuts()

    fun getToppingsAndBatters(): Flow<List<DonutWithToppings>> =
        dataBase.donutsDao.getToppingsOfDonuts()
}