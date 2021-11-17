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
    fun getToppingsOfDonut(idDonut: Int): Flow<List<DonutWithBatters>>

    @Transaction
    @Query("SELECT * FROM donut WHERE idDonut = (:idDonut)")
    fun getBattersOfDonut(idDonut: Int): Flow<List<DonutWithToppings>>
}