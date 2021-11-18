package com.example.localdatastorage.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBatters
import com.example.localdatastorage.model.room.entities.reletions.DonutWithToppings
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

    @Transaction
    @Query("SELECT * FROM donut WHERE idDonut = (:idDonut)")
    fun getToppingsOfDonut(idDonut: Int): Flow<DonutWithToppings>

    @Transaction
    @Query("SELECT * FROM donut WHERE idDonut = (:idDonut)")
    fun getBattersOfDonut(idDonut: Int): Flow<DonutWithBatters>


}