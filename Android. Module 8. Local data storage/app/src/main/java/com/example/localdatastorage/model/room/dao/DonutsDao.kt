package com.example.localdatastorage.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.localdatastorage.model.room.entities.Donut

@Dao
interface DonutsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun funInsert(vararg donuts: Donut)

}