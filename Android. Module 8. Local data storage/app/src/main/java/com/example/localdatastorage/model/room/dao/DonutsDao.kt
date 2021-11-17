package com.example.localdatastorage.model.room.dao

import androidx.room.*
import com.example.localdatastorage.model.room.entities.*
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBatters
import com.example.localdatastorage.model.room.entities.reletions.DonutWithToppings

@Dao
interface DonutsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonut(vararg donuts: Donut)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatter(vararg batter: Batter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopping(vararg topping: Topping)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutBatterCrossRef(vararg crossRef: DonutBatterCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonutToppingCrossRef(vararg crossRef: DonutToppingCrossRef)

    @Transaction
    @Query("SELECT * FROM donut WHERE idDonut = (:idDonut)")
    suspend fun getToppingsOfDonut(idDonut: Int): List<DonutWithBatters>

    @Transaction
    @Query("SELECT * FROM donut WHERE idDonut = (:idDonut)")
    suspend fun getBattersOfDonut(idDonut: Int): List<DonutWithToppings>
}