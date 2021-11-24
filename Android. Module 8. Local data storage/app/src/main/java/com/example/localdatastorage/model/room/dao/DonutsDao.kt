package com.example.localdatastorage.model.room.dao

import androidx.room.*
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBattersAndToppings
import kotlinx.coroutines.flow.Flow

@Dao
interface DonutsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonut(donut: Donut)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatter(batter: Batter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopping(topping: Topping)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutBatterCrossRef(crossRef: DonutBatterCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutToppingCrossRef(crossRef: DonutToppingCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonuts(donuts: List<Donut>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatters(batters: List<Batter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToppings(toppings: List<Topping>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutBatterCrossRefs(crossRefs: List<DonutBatterCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutToppingCrossRefs(crossRefs: List<DonutToppingCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutsWithBattersAndToppings(
        donuts: List<Donut>,
        batters: List<Batter>,
        toppings: List<Topping>,
        donutBatterCrossRefs: List<DonutBatterCrossRef>,
        donutToppingCrossRefs: List<DonutToppingCrossRef>
    )

    @Query("DELETE FROM donutbattercrossref WHERE idDonut = (:idDonut)")
    suspend fun deleteDonutBatterCrossRef(idDonut: Int)

    @Query("DELETE FROM donuttoppingcrossref WHERE idDonut = (:idDonut)")
    suspend fun deleteDonutToppingCrossRef(idDonut: Int)

    @Transaction
    @Query("SELECT * FROM donut WHERE idDonut = (:idDonut)")
    suspend fun getBattersAndToppingsOfDonut(idDonut: Int): DonutWithBattersAndToppings

    @Transaction
    @Query("SELECT * FROM donut")
    fun getBattersAndToppingsOfDonuts(): Flow<List<DonutWithBattersAndToppings>>

}