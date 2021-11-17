package com.example.localdatastorage.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localdatastorage.model.room.dao.DonutsDao
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.Topping


//@Database(
//    entities = [
//        Donut::class,
//        Topping::class,
//    ],
//    version = 1
//)
//abstract class DonutDataBase : RoomDatabase() {
//
//    abstract val donutsDao: DonutsDao
//}