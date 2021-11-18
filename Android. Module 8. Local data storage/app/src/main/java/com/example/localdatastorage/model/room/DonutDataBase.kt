package com.example.localdatastorage.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.localdatastorage.model.room.dao.DonutsDao
import com.example.localdatastorage.model.room.entities.*


@Database(
    entities = [
        Donut::class,
        Topping::class,
        Batter::class,
        DonutBatterCrossRef::class,
        DonutToppingCrossRef::class
    ],
    version = 1
)
abstract class DonutDataBase : RoomDatabase() {

    abstract val donutsDao: DonutsDao

    companion object {
        @Volatile
        private var INSTANCE: DonutDataBase? = null

        fun getInstance(context: Context): DonutDataBase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DonutDataBase::class.java,
                    "database-donut"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}